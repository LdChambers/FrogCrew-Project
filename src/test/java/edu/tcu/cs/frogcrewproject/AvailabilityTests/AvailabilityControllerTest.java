package edu.tcu.cs.frogcrewproject.AvailabilityTests;

import edu.tcu.cs.frogcrewproject.Controller.AvailabilityController;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewproject.dto.AvailabilityDto;
import edu.tcu.cs.frogcrewproject.service.AvailabilityService;
import edu.tcu.cs.frogcrewproject.config.TestSecurityConfig;
import edu.tcu.cs.frogcrewproject.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private AvailabilityDto dto;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        dto = new AvailabilityDto();
        dto.setCrewMemberId(1L);
        dto.setGameId(2L);
        dto.setAvailable(true);
        dto.setComment("Available after class");

        // Mock authentication and JWT token generation
        Authentication authentication = new UsernamePasswordAuthenticationToken("testuser", "password");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        jwtToken = "mock.jwt.token";
        when(jwtTokenProvider.createToken(authentication)).thenReturn(jwtToken);
    }

    @Test
    void shouldSubmitAvailabilitySuccessfully() throws Exception {
        mockMvc.perform(post("/api/availability/submit")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Availability submitted successfully."));

        verify(availabilityService, times(1))
                .submitAvailability(argThat(availabilityDto -> availabilityDto.getCrewMemberId().equals(1L) &&
                        availabilityDto.getGameId().equals(2L) &&
                        availabilityDto.isAvailable() &&
                        "Available after class".equals(availabilityDto.getComment())));
    }
}
