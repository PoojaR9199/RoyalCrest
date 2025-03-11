package com.royalcrest.RoyalCrestHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.royalcrest.RoyalCrestHotel.entity.Booking;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//Not return data which gives null
public class RoomDTO {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingDTO> bookings;

}
