package com.royalcrest.RoyalCrestHotel.service.interfac;

import com.royalcrest.RoyalCrestHotel.dto.Response;
import com.royalcrest.RoyalCrestHotel.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
