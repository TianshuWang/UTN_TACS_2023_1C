package com.tacs.telebot.validator;

import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import com.tacs.telebot.utils.Utils;
import org.springframework.stereotype.Component;

@Component
public class BodyValidator extends MessageValidator{


    public void validate(Message message) {
        boolean needsToValidate = Type.AUTH_AUTHENTICATION.name().equals(message.getType()) ||
                Type.AUTH_REGISTER.name().equals(message.getType()) ||
                Type.EVENTS_CREATE.name().equals(message.getType());

        if(needsToValidate && !Utils.isBodyValid(message)){
            throw new RuntimeException("The body is invalid");
        }
    }

}
