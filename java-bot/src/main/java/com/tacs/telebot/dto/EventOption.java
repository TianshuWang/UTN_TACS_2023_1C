package com.tacs.telebot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author tianshuwang
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventOption {
    @JsonProperty("id")
    private String id = null;
    @JsonProperty("date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date  dateTime;

    @JsonProperty("vote_quantity")
    private long voteQuantity;

    @JsonProperty("vote_users")
    private List<UserData> voteUserData;

    @JsonProperty("update_time")
    private Date updateDate;

    @JsonProperty("event_name")
    private String eventName;

}
