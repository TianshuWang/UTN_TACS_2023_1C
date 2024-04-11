package com.tacs.telebot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author tianshuwang
 */
@Data
@Builder
@NoArgsConstructor
public class Register {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("password_confirmation")
    private String passwordConfirmation;

    @JsonCreator
    public Register(@JsonProperty(value = "first_name", required = true)String firstName,
                    @JsonProperty(value = "last_name", required = true)String lastName,
                    @JsonProperty(value = "username", required = true)String username,
                    @JsonProperty(value = "password", required = true)String password,
                    @JsonProperty(value = "password_confirmation", required = true)String passwordConfirmation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

}
