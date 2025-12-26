package com.example.UberBooking_Service.services;

import com.example.UberBooking_Service.dto.CreateBookingDto;
import com.example.UberBooking_Service.dto.CreateBookingResponseDto;
import com.example.UberProject_EntityService.models.Booking;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);
}
