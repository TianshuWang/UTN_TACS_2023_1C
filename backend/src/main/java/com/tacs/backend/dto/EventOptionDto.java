package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class EventOptionDto {
    @JsonProperty("id")
    @Schema(description = "Event option id", hidden = true)
    private String id;
    @JsonProperty(value = "date_time", required = true)
    @NotBlank(message = "Date time can not be blank")
    @Schema(description = "Event option date time", example = "2023-05-06T00:52")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;

    @JsonProperty("vote_quantity")
    @Schema(description = "Event vote quantity", hidden = true)
    private long voteQuantity;


    @JsonProperty("update_time")
    @Schema(description = "Event option update time", hidden = true)
    private Date updateDate;

    @JsonProperty("event_name")
    @Schema(description = "Event name", hidden = true)
    private String eventName;

}
