package com.tacs.telebot.validator;



import com.tacs.telebot.dto.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageValidatorChain {
    private static final List<MessageValidator> VALIDATORS = new ArrayList<>();

    public static void register(MessageValidator validator) {
        VALIDATORS.add(validator);
    }

    public static void validate(Message message){
        VALIDATORS.forEach(v -> v.validate(message));
    }
}
