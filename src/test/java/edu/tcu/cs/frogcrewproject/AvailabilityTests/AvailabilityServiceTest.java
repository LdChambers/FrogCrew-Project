package edu.tcu.cs.frogcrewproject.AvailabilityTests;

import edu.tcu.cs.frogcrewproject.service.AvailabilityService;
import edu.tcu.cs.frogcrewproject.dto.AvailabilityDto;
import edu.tcu.cs.frogcrewproject.entity.Availability;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import edu.tcu.cs.frogcrewproject.repository.AvailabilityRepository;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private CrewMemberRepository crewMemberRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitAvailability_shouldSaveAvailability() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setCrewMemberId(1L);
        dto.setGameId(2L);
        dto.setAvailable(true);
        dto.setComment("I'm free after 6PM");

        CrewMember mockCrew = new CrewMember();
        mockCrew.setId(1L);
        Game mockGame = new Game();
        mockGame.setId(2L);

        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(mockCrew));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(mockGame));

        availabilityService.submitAvailability(dto);

        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }

    @Test
    void submitAvailability_shouldThrowIfCrewNotFound() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setCrewMemberId(999L);
        dto.setGameId(2L);
        when(crewMemberRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            availabilityService.submitAvailability(dto);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().contains("Crew member not found"));
        }

        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void submitAvailability_shouldThrowIfGameNotFound() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setCrewMemberId(1L);
        dto.setGameId(999L);

        CrewMember mockCrew = new CrewMember();
        mockCrew.setId(1L);

        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(mockCrew));
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        try {
            availabilityService.submitAvailability(dto);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().contains("Game not found"));
        }

        verify(availabilityRepository, never()).save(any());
    }
}
