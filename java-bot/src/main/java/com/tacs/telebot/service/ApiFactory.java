package com.tacs.telebot.service;



import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


/**
 * @author tianshuwang
 */
@RequiredArgsConstructor
public class ApiFactory {
    private final ApiService apiService;
    private Map<String, Function<Message, Object>> apiMethodsMap;

    @PostConstruct
    public void init() {
        apiMethodsMap = new HashMap<>(10);
        apiMethodsMap.put(Type.EVENTS_ALL.name(), apiService::getAllEvents);
        apiMethodsMap.put(Type.EVENTS_BY_ID.name(), apiService::getEventById);
        apiMethodsMap.put(Type.EVENTS_CREATE.name(), apiService::createEvent);
        apiMethodsMap.put(Type.EVENTS_REGISTER.name(), apiService::registerEvent);
        apiMethodsMap.put(Type.EVENTS_CHANGE_STATUS.name(), apiService::changeEventStatus);
        apiMethodsMap.put(Type.EVENTS_VOTE.name(), apiService::voteEventOption);
        apiMethodsMap.put(Type.AUTH_AUTHENTICATION.name(), apiService::authenticate);
        apiMethodsMap.put(Type.AUTH_REGISTER.name(), apiService::register);
        apiMethodsMap.put(Type.MONITOR_MARKETING_REPORT.name(), apiService::getCounterReport);
        apiMethodsMap.put(Type.MONITOR_OPTIONS_REPORT.name(), apiService::getOptionsReport);
    }

    public Function<Message, Object> getApiService(String type){
        return Optional.ofNullable(this.apiMethodsMap.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Type"));
    }
}
