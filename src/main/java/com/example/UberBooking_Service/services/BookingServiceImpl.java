package com.example.UberBooking_Service.services;

import com.example.UberBooking_Service.apis.LocationServiceApi;
import com.example.UberBooking_Service.apis.UberSocketApi;
import com.example.UberBooking_Service.dto.*;
import com.example.UberBooking_Service.repositories.BookingRepository;
import com.example.UberBooking_Service.repositories.DriverRepository;
import com.example.UberBooking_Service.repositories.PassengerRepository;
import com.example.UberProject_EntityService.models.Booking;
import com.example.UberProject_EntityService.models.BookingStatus;
import com.example.UberProject_EntityService.models.Driver;
import com.example.UberProject_EntityService.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final RestTemplate restTemplate;
    private final UberSocketApi uberSocketApi;
    private static final String LOCATION_SERVICE="http://localhost:7777";
    private final LocationServiceApi locationServiceApi;

    public BookingServiceImpl(BookingRepository bookingRepository, PassengerRepository passengerRepository,LocationServiceApi locationServiceApi,DriverRepository driverRepository,UberSocketApi uberSocketApi){
        this.bookingRepository=bookingRepository;
        this.passengerRepository=passengerRepository;
        this.restTemplate=new RestTemplate();
        this.locationServiceApi=locationServiceApi;
        this.driverRepository=driverRepository;
        this.uberSocketApi=uberSocketApi;
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
       Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());

        Booking booking=Booking
                .builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .passenger(passenger.get())
                .build();
        Booking newBooking=bookingRepository.save(booking);

        NearbyDriversRequestDto request = NearbyDriversRequestDto
                .builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        processNearbyDriversAsync(request,bookingDetails.getPassengerId());
//
//       ResponseEntity<DriverLocationDto[]> result= restTemplate.postForEntity(LOCATION_SERVICE+"/api/v1/location/nearby/drivers",request,DriverLocationDto[].class);
//
//       List<DriverLocationDto> driverLocations= Arrays.asList(result.getBody());
//
//       if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
//           driverLocations.forEach(driverLocationDto -> {
//               System.out.println(driverLocationDto.getDriverId() + " " + "lat: " +driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
//           });
//
//       }

        return CreateBookingResponseDto
                .builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {
        Optional<Driver> driver=driverRepository.findById(bookingRequestDto.getDriverId().get());
        bookingRepository.updateBookingStatusAndDriverById(bookingId, bookingRequestDto.getStatus(), driver.get());
        Optional<Booking> booking=bookingRepository.findById(bookingId);
        return UpdateBookingResponseDto
                .builder()
                .bookingId(bookingId)
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }

    private void processNearbyDriversAsync(NearbyDriversRequestDto requestDto,Long passengerId){
        Call<DriverLocationDto[]> call=locationServiceApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DriverLocationDto> driverLocations= Arrays.asList(response.body());
                    driverLocations.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                    });

                    raiseRideRequestAsync(RideRequestDto.builder().passengerId(passengerId).build());
                }else{
                    System.out.println("Request failed" + response.message());
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void raiseRideRequestAsync(RideRequestDto requestDto){
        Call<Boolean> call=uberSocketApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean result = response.body();
                    System.out.println("Driver response is " + result.toString());
                }else{
                    System.out.println("Request failed" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
