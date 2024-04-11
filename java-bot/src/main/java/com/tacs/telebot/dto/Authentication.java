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
public class Authentication {
    @JsonProperty(value = "username", required = true)
    private String username;
    @JsonProperty(value = "password", required = true)
    private String password;

    @JsonCreator
    public Authentication(@JsonProperty(value = "username", required = true) String username,
                          @JsonProperty(value = "password", required = true) String password) {
        this.username = username;
        this.password = password;
    }
}
