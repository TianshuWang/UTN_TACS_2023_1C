package com.tacs.telebot.service;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiManager {

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/authentication")
    Call<Object> authenticate(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/register")
    Call<Object> register(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/v1/events")
    Call<Object> createEvent(@Header("Authorization") String authHeader, @Body RequestBody body);

    @Headers({"Content-Type: application/json"})
    @GET("/v1/events")
    Call<Object> getAllEvents(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @GET("/v1/events/{id}")
    Call<Object> getEventById(@Header("Authorization") String authHeader, @Path("id") String id);

    @Headers("Content-Type: application/json")
    @PATCH("/v1/events/{id}/user")
    Call<Object> registerEvent(@Header("Authorization") String authHeader, @Path("id") String id);

    @Headers("Content-Type: application/json")
    @PATCH("/v1/events/{eventId}")
    Call<Object> changeEventStatus(@Header("Authorization") String authHeader, @Path("eventId") String eventId, @Query("status") String status);

    @Headers("Content-Type: application/json")
    @PATCH("/v1/events/{eventId}/options/{optionId}/vote")
    Call<Object> voteEventOption(@Header("Authorization") String authHeader, @Path("eventId") String eventId, @Path("optionId") String optionId);

    @Headers("Content-Type: application/json")
    @GET("/v1/monitor/ratios")
    Call<Object> getCounterReport(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @GET("/v1/monitor/options")
    Call<Object> getOptionsReport(@Header("Authorization") String authHeader);
}
