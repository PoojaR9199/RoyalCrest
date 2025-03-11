package com.royalcrest.RoyalCrestHotel.service.impl;

import com.royalcrest.RoyalCrestHotel.dto.BookingDTO;
import com.royalcrest.RoyalCrestHotel.dto.Response;
import com.royalcrest.RoyalCrestHotel.dto.RoomDTO;
import com.royalcrest.RoyalCrestHotel.entity.Booking;
import com.royalcrest.RoyalCrestHotel.entity.Room;
import com.royalcrest.RoyalCrestHotel.entity.User;
import com.royalcrest.RoyalCrestHotel.exception.OurException;
import com.royalcrest.RoyalCrestHotel.repository.BookingRepository;
import com.royalcrest.RoyalCrestHotel.repository.RoomRepository;
import com.royalcrest.RoyalCrestHotel.repository.UserRepository;
import com.royalcrest.RoyalCrestHotel.service.interfac.IBookingService;
import com.royalcrest.RoyalCrestHotel.service.interfac.IRoomService;
import com.royalcrest.RoyalCrestHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response=new Response();
        try {
           if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
               throw new IllegalArgumentException("CheckIn date must come after check out date");
           }
           Room room = roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
           User user = userRepository.findById(userId).orElseThrow(()->new OurException("User not found"));
           List<Booking> existingBookings = room.getBookings();

           if(!roomIsAvailable(bookingRequest,existingBookings)){
               throw new OurException("Room not available for selected date range");
           }

           bookingRequest.setRoom(room);
           bookingRequest.setUser(user);
           String bookingConfirmationCode=Utils.generateRandomConfirmationCode(10);
           bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
           bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking successfully");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a booking " +e.getMessage());

        }
        return response;
    }



    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response=new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Booking not found"));
            BookingDTO bookingDTO=Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking,true);
            response.setStatusCode(200);
            response.setMessage("Booking successfully");
            response.setBooking(bookingDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a booking " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response=new Response();
        try {
            List<Booking> bookingList= bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDTO> bookingDTOList=Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("All Booking have been fetched successfully");
            response.setBookingList(bookingDTOList);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching the bookings " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response=new Response();
        try {
            bookingRepository.findById(bookingId).orElseThrow(()->new OurException("No bookings Found"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Booking deleted successfully");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting the booking " +e.getMessage());

        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
