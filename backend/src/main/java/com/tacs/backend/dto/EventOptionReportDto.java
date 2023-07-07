package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventOptionReportDto {
    @JsonProperty("id")
    @Schema(description = "Event option id", hidden = true)
    private String id;
    @JsonProperty("date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(description = "Event option date time")
    private Date dateTime;

    @JsonProperty("last_update_time")
    @Schema(description = "Event option last update time")
    private Date lastUpdateDate;

    @JsonProperty("vote_quantity")
    @Schema(description = "Quantity of votes")
    private long voteQuantity;

    @JsonProperty("event_name")
    private String eventName;
}
