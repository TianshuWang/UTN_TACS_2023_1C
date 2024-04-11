package com.tacs.telebot.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.tacs.telebot.dto.Message;
import com.tacs.telebot.validator.MessageValidatorChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author tianshuwang
 */
@Service
@RequiredArgsConstructor
public class TelebotService {
    private static final String LENGTH_ERROR = "Result is too long";
    private static final int BYTES = 4096;
    private final ObjectWriter objectWriter;
    private final ApiFactory apiFactory;

    public String getResult(Message message)  {

        String finalResult;
        try {
            MessageValidatorChain.validate(message);
            Object response = apiFactory.getApiService(message.getType()).apply(message);
            String result = response.getClass().equals(String.class) ? (String) response : objectWriter.writeValueAsString(response);
            finalResult = result.getBytes().length > BYTES ? LENGTH_ERROR : result;
        }
        catch(Exception e) {
            return e.getMessage();
        }

        return finalResult;
    }

}
