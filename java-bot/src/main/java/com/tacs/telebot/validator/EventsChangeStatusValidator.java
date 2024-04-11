package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsChangeStatusValidator extends MessageValidator {

    public void validate(Message message) {
        boolean isLackRequiredFields = StringUtils.isBlank(message.getEventId()) || StringUtils.isBlank(message.getStatus());
        if(Type.EVENTS_CHANGE_STATUS.name().equals(message.getType()) && isLackRequiredFields){
            throw new RuntimeException("Event Id or Status is required");
        }
    }
}
