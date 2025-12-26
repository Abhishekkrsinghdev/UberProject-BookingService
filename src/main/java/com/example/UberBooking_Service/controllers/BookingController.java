package com.example.UberBooking_Service.controllers;

import com.example.UberBooking_Service.dto.CreateBookingDto;
import com.example.UberBooking_Service.dto.CreateBookingResponseDto;
import com.example.UberBooking_Service.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }
    @PostMapping
    public ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody  CreateBookingDto createBookingDto){
        return new ResponseEntity<>(this.bookingService.createBooking(createBookingDto), HttpStatus.CREATED);
    }
}
