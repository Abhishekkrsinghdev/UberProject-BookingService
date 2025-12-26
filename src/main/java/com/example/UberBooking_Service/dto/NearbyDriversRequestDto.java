package com.example.UberBooking_Service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriversRequestDto {
    private Double latitude;
    private Double longitude;
}
