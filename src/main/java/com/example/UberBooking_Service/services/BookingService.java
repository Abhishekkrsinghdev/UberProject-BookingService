package com.example.UberBooking_Service.services;

import com.example.UberBooking_Service.dto.CreateBookingDto;
import com.example.UberBooking_Service.dto.CreateBookingResponseDto;
import com.example.UberBooking_Service.dto.UpdateBookingRequestDto;
import com.example.UberBooking_Service.dto.UpdateBookingResponseDto;
import com.example.UberProject_EntityService.models.Booking;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto,Long bookingId);
}
