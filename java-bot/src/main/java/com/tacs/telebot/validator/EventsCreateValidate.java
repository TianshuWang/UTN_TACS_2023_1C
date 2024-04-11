package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsCreateValidate extends MessageValidator {

    public void validate(Message message) {
        if(Type.EVENTS_CREATE.name().equals(message.getType()) && StringUtils.isBlank(message.getBody())){
            throw new RuntimeException("Body is required");
        }
    }
}
