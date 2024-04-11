package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsByIdValidator extends MessageValidator {

    public void validate(Message message) {
       if(Type.EVENTS_BY_ID.name().equals(message.getType()) && StringUtils.isBlank(message.getEventId())){
           throw new RuntimeException("Event Id is required");
       }
    }
}
