package com.tacs.telebot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.tacs.telebot.dto.*;
import com.tacs.telebot.service.TelebotService;
import com.tacs.telebot.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;



/**
 * @author tianshuwang
 */
@BotController
@RequiredArgsConstructor
@Slf4j
public class TelebotController implements TelegramMvcController {
    private static final String BEARER = "Bearer ";
    @Value("${bot.token}")
    private String token;
    private final TelebotService telebotService;
    private final ObjectWriter objectWriter;
    private final ObjectMapper objectMapper;

    @Override
    public String getToken() {
        return token;
    }


    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat, Update update) {
        return new SendMessage(chat.id(), "Hello, " + user.firstName() + "! Send '/help' to get more info.");
    }

    @MessageRequest("/help" )
    public String help() {
        return Utils.helpMessage();
    }

    @MessageRequest("/login/{password}" )
    public String authentication(@BotPathVariable("password") String password, User user) throws JsonProcessingException {
        Authentication authentication = Authentication.builder().username(user.id().toString()).password(password).build();
        Message message = Message.builder()
                        .type(Type.AUTH_AUTHENTICATION.name())
                        .body(objectWriter.writeValueAsString(authentication)).build();
        String result = telebotService.getResult(message);
        return Utils.isValidJson(result) ? objectMapper.readValue(telebotService.getResult(message), AuthenticationResponse.class).getAccessToken()
                :result;
    }

    @MessageRequest("/register/{password}" )
    public String register(@BotPathVariable("password") String password, User user) throws JsonProcessingException {
        Register register = Register.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .username(user.username()).password(password)
                .passwordConfirmation(password).build();
        Message message = Message.builder()
                .type(Type.AUTH_REGISTER.name())
                .body(objectWriter.writeValueAsString(register)).build();
        String result = telebotService.getResult(message);
        return Utils.isValidJson(result) ? objectMapper.readValue(telebotService.getResult(message), AuthenticationResponse.class).getAccessToken()
                :result;
    }

    @MessageRequest("/new_event/{token}/{body}" )
    public String createEvent(@BotPathVariable("token") String token, @BotPathVariable("body") String body) {
        Message message = Message.builder()
                .type(Type.EVENTS_CREATE.name())
                .token(BEARER + token)
                .body(body).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/all_events/{token}" )
    public String getAllEvents(@BotPathVariable("token") String token) {
        Message message = Message.builder()
                .type(Type.EVENTS_ALL.name())
                .token(BEARER + token).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/event_by_id/{token}/{eventId}" )
    public String getEventById(@BotPathVariable("token") String token, @BotPathVariable("eventId") String eventId) {
        Message message = Message.builder()
                .type(Type.EVENTS_BY_ID.name())
                .token(BEARER + token)
                .eventId(eventId).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/vote_event_option/{token}/{eventId}/{optionId}")
    public String voteEventOption(@BotPathVariable("token") String token, @BotPathVariable("eventId") String eventId, @BotPathVariable("optionId") String optionId) {
        Message message = Message.builder()
                .type(Type.EVENTS_VOTE.name())
                .token(BEARER + token)
                .eventId(eventId)
                .optionId(optionId).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/register_event/{token}/{eventId}")
    public String registerEvent(@BotPathVariable("token") String token, @BotPathVariable("eventId") String eventId) {
        Message message = Message.builder()
                .type(Type.EVENTS_REGISTER.name())
                .token(BEARER + token)
                .eventId(eventId).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/change_event_status/{token}/{eventId}/{status}")
    public String changeEventStatus(@BotPathVariable("token") String token, @BotPathVariable("eventId") String eventId, @BotPathVariable("status") String status) {
        Message message = Message.builder()
                .type(Type.EVENTS_CHANGE_STATUS.name())
                .token(BEARER + token)
                .eventId(eventId)
                .status(status).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/events_marketing_report/{token}")
    public String getCounterReport(@BotPathVariable("token") String token) {
        Message message = Message.builder()
                .type(Type.MONITOR_MARKETING_REPORT.name())
                .token(BEARER + token).build();
        return telebotService.getResult(message);
    }

    @MessageRequest("/options_report/{token}")
    public String getOptionsReport(@BotPathVariable("token") String token) {
        Message message = Message.builder()
                .type(Type.MONITOR_OPTIONS_REPORT.name())
                .token(BEARER + token).build();
        return telebotService.getResult(message);
    }

}
