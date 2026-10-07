package com.ecommerce.controller;

import com.ecommerce.model.dto.LoginRequest;
import com.ecommerce.model.dto.TwoFactorChallengeResponse;
import com.ecommerce.security.jwt.JwtTokenProvider;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.UserService;
import com.ecommerce.security.service.TotpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    @MockBean AuthService authService;
    @MockBean UserService userService;
    @MockBean TotpService totpService;
    @MockBean JwtTokenProvider jwtTokenProvider;

    @Test
    void loginReturnsTwoFactorChallengeWhenEnabled() throws Exception {
        when(authService.login(any(), any()))
                .thenReturn(new TwoFactorChallengeResponse(true, "challenge-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(
                                new LoginRequest("a@b.com", "Passw0rd!", "tenant1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.twoFactorRequired").value(true));
    }

    @Test
    void loginRejectsBlankTenant() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"a@b.com\",\"password\":\"x\",\"tenantId\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
