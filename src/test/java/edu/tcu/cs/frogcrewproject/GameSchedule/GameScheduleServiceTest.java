package edu.tcu.cs.frogcrewproject.GameSchedule;

import edu.tcu.cs.frogcrewproject.dto.GameDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import edu.tcu.cs.frogcrewproject.mapper.GameMapper;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.repository.GameRepository;
import edu.tcu.cs.frogcrewproject.service.GameScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameScheduleServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CrewMemberRepository crewMemberRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameScheduleService gameScheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGames() {
        Game game = new Game();
        GameDto gameDto = new GameDto();
        when(gameRepository.findAll()).thenReturn(List.of(game));
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        List<GameDto> result = gameScheduleService.findAllGames();

        assertThat(result).hasSize(1);
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testAddGame() {
        GameDto dto = new GameDto();
        Game entity = new Game();
        Game savedEntity = new Game();

        when(gameMapper.toEntity(dto, Set.of())).thenReturn(entity);
        when(gameRepository.save(entity)).thenReturn(savedEntity);
        when(gameMapper.toDto(savedEntity)).thenReturn(dto);

        GameDto result = gameScheduleService.addGame(dto);

        assertThat(result).isEqualTo(dto);
        verify(gameRepository).save(entity);
    }

    @Test
    void testAssignCrewToGameSuccess() {
        Long gameId = 1L;
        List<Long> crewIds = List.of(101L);
        Game game = new Game();
        CrewMember crew = new CrewMember();
        crew.setId(101L);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(crewMemberRepository.findAllById(crewIds)).thenReturn(List.of(crew));

        gameScheduleService.assignCrewToGame(gameId, crewIds);

        verify(gameRepository).save(game);
        assertThat(game.getAssignedCrew()).contains(crew);
    }

    @Test
    void testAssignCrewToGameThrowsIfGameMissing() {
        Long gameId = 404L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                gameScheduleService.assignCrewToGame(gameId, List.of(1L)));

        verify(gameRepository, never()).save(any());
    }
}

