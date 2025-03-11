package com.royalcrest.RoyalCrestHotel.service.impl;

import com.royalcrest.RoyalCrestHotel.dto.LoginRequest;
import com.royalcrest.RoyalCrestHotel.dto.Response;
import com.royalcrest.RoyalCrestHotel.dto.UserDTO;
import com.royalcrest.RoyalCrestHotel.entity.User;
import com.royalcrest.RoyalCrestHotel.exception.OurException;
import com.royalcrest.RoyalCrestHotel.repository.UserRepository;
import com.royalcrest.RoyalCrestHotel.service.interfac.IUserService;
import com.royalcrest.RoyalCrestHotel.utils.JWTUtils;
import com.royalcrest.RoyalCrestHotel.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response=new Response();
        try {
            System.out.println("Register called");
            if(user.getRole()==null || user.getRole().isBlank()){ //---------------------------------If no role is assignes, assign as a USER
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail()+" Already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser=userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured during user registration " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response=new Response();
        try {
            System.out.println("login called");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("User not found"));
            var token=jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
            response.setMessage("Login successfull");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured during user Login " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response=new Response();
        try {
            List<User> userList=userRepository.findAll();
            List<UserDTO> userDTOList=Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Fetch all users successfull");
            response.setUsersList(userDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response=new Response();
        try {
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Fetch user booking successfull");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching user bookings " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response=new Response();
        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Deletion successfull");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting a user " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response=new Response();
        try {
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Fetch successfull");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching a user detail " +e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response=new Response();
        try {
            User user=userRepository.findByEmail(email).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("GetMyInfo successfull");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured while fetching info " +e.getMessage());
        }
        return response;
    }
}
