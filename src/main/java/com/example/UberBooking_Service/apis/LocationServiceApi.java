package com.example.UberBooking_Service.apis;

import com.example.UberBooking_Service.dto.DriverLocationDto;
import com.example.UberBooking_Service.dto.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("/api/v1/location/nearby/drivers")
    Call<DriverLocationDto[]> getNearbyDrivers(@Body NearbyDriversRequestDto requestDto);
}
