package com.tacs.telebot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tacs.telebot.dto.*;

import okhttp3.ResponseBody;

import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author tianshuwang
 */
public class Utils {
    protected static final Map<String, Class<?>> CLASS_MAP = new HashMap<>();
    static {
        CLASS_MAP.put(Type.EVENTS_CREATE.name(), Event.class);
        CLASS_MAP.put(Type.AUTH_AUTHENTICATION.name(), Authentication.class);
        CLASS_MAP.put(Type.AUTH_REGISTER.name(), Register.class);
    }
    
    public static String getResponseErrorMessage(Response<?> response) {
        final ResponseBody errorBody = response.errorBody();
        final String errorMessage;
        try {
            String error = Objects.isNull(errorBody) ? response.message() : errorBody.string();
            ObjectMapper mapper = new ObjectMapper();
            ExceptionResponse exceptionResponse = mapper.readValue(error, ExceptionResponse.class);
            errorMessage = exceptionResponse.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return errorMessage;
    }

    public static boolean isValidJson(String body) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(body);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isBodyValid(Message message) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(message.getBody(), CLASS_MAP.get(message.getType()));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String helpMessage() {
        return "Commands Help: \n"
                .concat("******** /${body} in JSON format ***********\n")
                .concat("- Log In: /login/${password}\n")
                .concat("- Sign Up: /register/${password}\n")
                .concat("- Get All Events: /all_events/${token}\n")
                .concat("- Get Event By Id: /event_by_id/${token}/${eventId}\n")
                .concat("- Create New Event: /new_event/${token}/${body}\n")
                .concat("- Change Event Status: /change_event_status/${token}/${eventId}/${status}\n")
                .concat("- Vote Event: /vote_event_option/${token}/${eventId}/${optionId}\n")
                .concat("- Register Event: /register/${token}/${eventId}\n")
                .concat("- Get Marketing Report: /events_marketing_report/${token}\n")
                .concat("- Get Options Report: /options_report/${token}\n");

    }
}
