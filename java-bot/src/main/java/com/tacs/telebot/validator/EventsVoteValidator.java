package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsVoteValidator extends MessageValidator {

    public void validate(Message message) {
        boolean isLackRequiredFields = StringUtils.isBlank(message.getEventId()) || StringUtils.isBlank(message.getOptionId());
        if(Type.EVENTS_VOTE.name().equals(message.getType()) && isLackRequiredFields){
            throw new RuntimeException("Event Id and Option Id are required");
        }
    }

}
