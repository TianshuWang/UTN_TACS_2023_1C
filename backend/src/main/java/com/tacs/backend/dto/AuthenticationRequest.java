package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRequest {
    @JsonProperty(value = "username", required = true)
    @NotBlank(message = "Username can not be blank")
    @Schema(description = "Username", example = "juan.perez")
    private String username;
    @JsonProperty(value = "password", required = true)
    @NotBlank(message = "Password can not be blank")
    @Schema(description = "Password", example = "mksiug_865K")
    private String password;
}
