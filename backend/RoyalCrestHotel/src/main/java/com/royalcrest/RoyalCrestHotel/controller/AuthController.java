package com.royalcrest.RoyalCrestHotel.controller;

import com.royalcrest.RoyalCrestHotel.dto.LoginRequest;
import com.royalcrest.RoyalCrestHotel.dto.Response;
import com.royalcrest.RoyalCrestHotel.entity.User;
import com.royalcrest.RoyalCrestHotel.service.interfac.IUserService;
import com.royalcrest.RoyalCrestHotel.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @Autowired
    private JWTUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        Response response=userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        System.out.println("login inside controller");
        Response response=userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
