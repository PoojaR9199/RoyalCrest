package com.royalcrest.RoyalCrestHotel.service.impl;

import com.royalcrest.RoyalCrestHotel.dto.LoginRequest;
import com.royalcrest.RoyalCrestHotel.dto.Response;
import com.royalcrest.RoyalCrestHotel.dto.RoomDTO;
import com.royalcrest.RoyalCrestHotel.dto.UserDTO;
import com.royalcrest.RoyalCrestHotel.entity.Room;
import com.royalcrest.RoyalCrestHotel.entity.User;
import com.royalcrest.RoyalCrestHotel.exception.OurException;
import com.royalcrest.RoyalCrestHotel.repository.BookingRepository;
import com.royalcrest.RoyalCrestHotel.repository.RoomRepository;
import com.royalcrest.RoyalCrestHotel.repository.UserRepository;
import com.royalcrest.RoyalCrestHotel.service.AwsS3Service;
import com.royalcrest.RoyalCrestHotel.service.interfac.IRoomService;
import com.royalcrest.RoyalCrestHotel.service.interfac.IUserService;
import com.royalcrest.RoyalCrestHotel.utils.JWTUtils;
import com.royalcrest.RoyalCrestHotel.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response=new Response();
        try {
            String imageUrl = awsS3Service.saveImageToS3(photo); //fetch image
            Room room=new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom =roomRepository.save(room);
            RoomDTO roomDTO= Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Room added successfull");
            response.setRoom(roomDTO);

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();

    }

    @Override
    public Response getAllRooms() {
        Response response=new Response();
        try {
            List<Room> roomList=roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoomList(roomDTOList);

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response=new Response();
        try {
            roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Room info deleted successfully");
        }
        catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description,String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response=new Response();
        try {
            String imageUrl = null;
            if(photo !=null && !photo.isEmpty()){
                imageUrl=awsS3Service.saveImageToS3(photo);
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            if(roomType!=null) room.setRoomType(roomType);
            if(description!=null) room.setRoomDescription(description);
            if(roomPrice!=null) room.setRoomPrice(roomPrice);
            if(imageUrl!=null) room.setRoomPhotoUrl(imageUrl);
            Room updatedRoom=roomRepository.save(room);
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room info updated successfully");
            response.setRoom(roomDTO);

        }
        catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error updating a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response=new Response();
        try {
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("Room info fetched successfully");
            response.setRoom(roomDTO);
        }
        catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response=new Response();
        try {
            List<Room> avialableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate,roomType);
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(avialableRooms);
            response.setStatusCode(200);
            response.setMessage("Room info fetched successfully");
            response.setRoomList(roomDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a room " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response=new Response();
        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Available Rooms fetched successfully");
            response.setRoomList(roomDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a room " +e.getMessage());

        }
        return response;
    }
}
