package com.tacs.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tacs.backend.security.ConfirmedField;
import com.tacs.backend.security.ValidPassword;
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
@ConfirmedField(originalField = "password", confirmationField = "passwordConfirmation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequest {
    @JsonProperty(value = "first_name", required = true)
    @NotBlank(message = "First name can not be blank")
    @Schema(description = "User first name", example = "Juan")
    private String firstName;
    @JsonProperty(value = "last_name", required = true)
    @NotBlank(message = "Last name can not be blank")
    @Schema(description = "User last name", example = "Perez")
    private String lastName;
    @JsonProperty(value = "username", required = true)
    @NotBlank(message = "Username can not be blank")
    @Schema(description = "Username", example = "juan.perez")
    private String username;
    @JsonProperty(value = "password", required = true)
    @NotBlank(message = "Password can not be blank")
    @ValidPassword
    @Schema(description = "Password", example = "mksiug_865K")
    private String password;
    @JsonProperty(value = "password_confirmation", required = true)
    @NotBlank(message = "Password confirmation can not be blank")
    @Schema(description = "Confirmation password", example = "mksiug_865K")
    private String passwordConfirmation;
}
