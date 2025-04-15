package edu.tcu.cs.frogcrewproject.GameSchedule;

import edu.tcu.cs.frogcrewproject.Controller.GameScheduleController;
import edu.tcu.cs.frogcrewproject.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewproject.dto.GameDto;
import edu.tcu.cs.frogcrewproject.service.GameScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(GameScheduleController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class GameScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameScheduleService gameScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    private GameDto mockGameDto;

    @BeforeEach
    void setUp() {
        mockGameDto = new GameDto();
        mockGameDto.setId(1L);
        mockGameDto.setOpponent("Baylor");
        mockGameDto.setSport("Football");
        mockGameDto.setVenue("TCU Stadium");
        mockGameDto.setGameDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    @WithMockUser
    void shouldGetAllGames() throws Exception {
        when(gameScheduleService.findAllGames()).thenReturn(List.of(mockGameDto));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].opponent").value("Baylor"));
    }

    @Test
    @WithMockUser
    void shouldAddGame() throws Exception {
        when(gameScheduleService.addGame(any(GameDto.class))).thenReturn(mockGameDto);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockGameDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.opponent").value("Baylor"));
    }

    @Test
    @WithMockUser
    void shouldAssignCrewToGame() throws Exception {
        when(gameScheduleService.assignCrew(any(Long.class), any(List.class))).thenReturn(mockGameDto);

        mockMvc.perform(post("/api/games/1/assign-crew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(1L, 2L))))
                .andExpect(status().isOk());

        verify(gameScheduleService, times(1)).assignCrew(eq(1L), any());
    }
}
