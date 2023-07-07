package com.tacs.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tacs.backend.dto.AuthenticationRequest;
import com.tacs.backend.dto.AuthenticationResponse;
import com.tacs.backend.dto.RegisterRequest;
import com.tacs.backend.exception.EntityNotFoundException;
import com.tacs.backend.exception.UserException;
import com.tacs.backend.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private MockMvc mvc;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    @BeforeEach
    void setup() {
        registerRequest = RegisterRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .username("juan.perez")
                .password("sjuy_987T")
                .passwordConfirmation("sjuy_987T")
                .build();
        authenticationRequest = AuthenticationRequest.builder()
                .username("juan.perez")
                .password("sjuy_987T")
                .build();
        authenticationResponse = AuthenticationResponse.builder()
                .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6IiwiaWF0IjoxNjgxNTczODcwLCJleHAiOjE2ODE2MTcwNzB9.M0LcXFOT3Vff17IGl9oRaMoAdWRxYI_twyZTONALA0U")
                .build();
        mvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    @DisplayName("Should return 200 when register a user")
    void itShouldReturnAuthenticationResponseWith200StatusCodeWhenCalledRegister() throws Exception {
        given(authenticationService.register(registerRequest)).willReturn(authenticationResponse);

        MockHttpServletResponse response = mvc.perform(post("/v1/auth/register")
                        .content(asJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(authenticationResponse));
    }

    @Test
    @DisplayName("Should return 400 when register a user already exists")
    void itShouldReturnErrorWith400StatusCodeWhenCalledRegisterAlreadyExists() throws Exception {
        given(authenticationService.register(registerRequest)).willThrow(new UserException("Username already exists"));

        MockHttpServletResponse response = mvc.perform(post("/v1/auth/register")
                        .content(asJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Username already exists");
    }

    @Test
    @DisplayName("Should return 200 when authenticate a user")
    void itShouldReturnAuthenticationResponseWith200StatusCodeWhenCalledAuthenticate() throws Exception {
        given(authenticationService.authenticate(authenticationRequest)).willReturn(authenticationResponse);

        MockHttpServletResponse response = mvc.perform(post("/v1/auth/authentication")
                        .content(asJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(authenticationResponse));
    }

    @Test
    @DisplayName("Should return 400 when authenticate a user not exists")
    void itShouldReturnErrorWith400StatusCodeWhenCalledAuthenticateNotExists() throws Exception {
        given(authenticationService.authenticate(authenticationRequest)).willThrow(new EntityNotFoundException("User not found"));

        MockHttpServletResponse response = mvc.perform(post("/v1/auth/authentication")
                        .content(asJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("User not found");
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
