package com.tacs.telebot.service;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.utils.Utils;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ApiService {
     public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
     private final ApiManager apiManager;

     public Object authenticate(Message message) {
          Response<Object> response;
          RequestBody requestBody = RequestBody.create(message.getBody(), JSON);
          try {
               Call<Object> request = apiManager.authenticate(requestBody);
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object register(Message message) {
          Response<Object> response;
          RequestBody requestBody = RequestBody.create(message.getBody(), JSON);
          try {
               Call<Object> request = apiManager.register(requestBody);
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object getAllEvents(Message message)  {
          Response<Object> response;
          try {
               Call<Object> request = apiManager.getAllEvents(message.getToken());
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object createEvent(Message message) {
          Response<Object> response;
          RequestBody requestBody = RequestBody.create(message.getBody(), JSON);
          try {
               Call<Object> request = apiManager.createEvent(message.getToken(), requestBody);
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object getEventById(Message message) {
          Call<Object> request = apiManager.getEventById(message.getToken(), message.getEventId());
          Response<Object> response;
          try {
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object registerEvent(Message message) {
          Call<Object> request = apiManager.registerEvent(message.getToken(), message.getEventId());
          Response<Object> response;
          try {
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object voteEventOption(Message message) {
          Call<Object> request = apiManager.voteEventOption(message.getToken(), message.getEventId(), message.getOptionId());
          Response<Object> response = null;
          try {
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object changeEventStatus(Message message)  {
          Call<Object> request = apiManager.changeEventStatus(message.getToken(), message.getEventId(), message.getStatus());
          Response<Object> response;
          try {
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }

     public Object getCounterReport(Message message) {
          Response<Object> response;
          try {
               Call<Object> request = apiManager.getCounterReport(message.getToken());
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body() : Utils.getResponseErrorMessage(response);
     }

     public Object getOptionsReport(Message message) {
          Call<Object> request = apiManager.getOptionsReport(message.getToken());
          Response<Object> response;
          try {
               response = request.execute();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return response.isSuccessful() ? response.body(): Utils.getResponseErrorMessage(response);
     }
}
