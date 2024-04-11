package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import com.tacs.telebot.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthValidate extends MessageValidator {

    public void validate(Message message) {
        boolean isAuthService = Type.AUTH_REGISTER.name().equals(message.getType()) || Type.AUTH_AUTHENTICATION.name().equals(message.getType());
        if(isAuthService && StringUtils.isBlank(message.getBody())){
            throw new RuntimeException("Body is required");
        }
    }
}
