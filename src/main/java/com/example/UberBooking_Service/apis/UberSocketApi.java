package com.example.UberBooking_Service.apis;

import com.example.UberBooking_Service.dto.DriverLocationDto;
import com.example.UberBooking_Service.dto.NearbyDriversRequestDto;
import com.example.UberBooking_Service.dto.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UberSocketApi {
    @POST("/api/socket/newride")
    Call<Boolean> raiseRideRequest(@Body RideRequestDto requestDto);
}
