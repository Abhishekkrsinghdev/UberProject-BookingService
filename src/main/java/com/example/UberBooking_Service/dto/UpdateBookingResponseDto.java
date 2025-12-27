package com.example.UberBooking_Service.dto;

import com.example.UberProject_EntityService.models.BookingStatus;
import com.example.UberProject_EntityService.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingResponseDto {
    private Long bookingId;
    private BookingStatus status;
    private Optional<Driver> driver;
}
