package com.example.UberBooking_Service.services;

import com.example.UberBooking_Service.dto.CreateBookingDto;
import com.example.UberBooking_Service.dto.CreateBookingResponseDto;
import com.example.UberBooking_Service.dto.DriverLocationDto;
import com.example.UberBooking_Service.dto.NearbyDriversRequestDto;
import com.example.UberBooking_Service.repositories.BookingRepository;
import com.example.UberBooking_Service.repositories.PassengerRepository;
import com.example.UberProject_EntityService.models.Booking;
import com.example.UberProject_EntityService.models.BookingStatus;
import com.example.UberProject_EntityService.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;
    private static final String LOCATION_SERVICE="http://localhost:7777";

    public BookingServiceImpl(BookingRepository bookingRepository, PassengerRepository passengerRepository){
        this.bookingRepository=bookingRepository;
        this.passengerRepository=passengerRepository;
        this.restTemplate=new RestTemplate();
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

       ResponseEntity<DriverLocationDto[]> result= restTemplate.postForEntity(LOCATION_SERVICE+"/api/v1/location/nearby/drivers",request,DriverLocationDto[].class);

       List<DriverLocationDto> driverLocations= Arrays.asList(result.getBody());

       if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
           driverLocations.forEach(driverLocationDto -> {
               System.out.println(driverLocationDto.getDriverId() + " " + "lat: " +driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
           });

       }

        return CreateBookingResponseDto
                .builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }
}
