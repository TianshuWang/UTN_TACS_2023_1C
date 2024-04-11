package com.tacs.telebot.validator;


import com.tacs.telebot.dto.Message;
import jakarta.annotation.PostConstruct;

public abstract class MessageValidator {
    public abstract void validate(Message message);
    @PostConstruct
    protected void init(){
        MessageValidatorChain.register(this);
    }
}
