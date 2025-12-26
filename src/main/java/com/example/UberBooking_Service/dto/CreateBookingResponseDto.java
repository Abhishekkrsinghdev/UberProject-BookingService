package com.example.UberBooking_Service.dto;

import com.example.UberProject_EntityService.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponseDto {
    private Long bookingId;

    private String bookingStatus;

    private Optional<Driver> driver;
}
