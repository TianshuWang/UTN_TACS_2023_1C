package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketingReportDto {
    @JsonProperty("events_count")
    @Schema(description = "Created events count")
    private long eventsCount;

    @JsonProperty("options_count")
    @Schema(description = "Most voted options count")
    private long optionsCount;
}
