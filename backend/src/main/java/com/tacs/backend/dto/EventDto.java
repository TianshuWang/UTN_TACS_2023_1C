package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {
    @JsonProperty("id")
    @Schema(description = "Event id", hidden = true)
    private String id;
    @JsonProperty(value = "name", required = true)
    @NotBlank(message = "Name can not be blank")
    @Schema(description = "Event name", example = "TACS")
    private String name;
    @JsonProperty("description")
    @Schema(description = "Event description", example = "TACS")
    private String description;

    @JsonProperty("status")
    @Schema(description = "Event status", hidden = true)
    private String status;

    @JsonProperty(value = "event_options", required = true)
    @Schema(description = "Event options")
    private Set<EventOptionDto> eventOptions;

    @JsonProperty("owner_user")
    @Schema(description = "Event owner user", hidden = true)
    private UserDto ownerUser;

    @JsonProperty("registered_users")
    @Schema(description = "Event registered users", hidden = true)
    private Set<UserDto> registeredUsers;

    @JsonProperty("create_date")
    @Schema(description = "Event create date", hidden = true)
    private Date createDate;

}
