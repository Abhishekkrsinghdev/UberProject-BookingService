package com.example.UberBooking_Service.configurations;

import com.example.UberBooking_Service.apis.LocationServiceApi;
import com.example.UberBooking_Service.apis.UberSocketApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


@Configuration
public class RetrofitConfig {
    private final EurekaClient eurekaClient;

    public RetrofitConfig(EurekaClient eurekaClient){
        this.eurekaClient=eurekaClient;
    }

    private String getServiceUrl(String serviceName){
        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi(ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("UBERLOCATION-SERVICE"))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }

    @Bean
    public UberSocketApi uberSocketApi(ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("CLIENTSOCKET-SERVICE"))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(UberSocketApi.class);
    }
}
