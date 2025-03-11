package com.royalcrest.RoyalCrestHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.royalcrest.RoyalCrestHotel.entity.Booking;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //Not return data which gives null
//Data Transfer Object
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookings=new ArrayList<>();
}
