package edu.tcu.cs.frogcrewproject.service;

import edu.tcu.cs.frogcrewproject.dto.GameDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import edu.tcu.cs.frogcrewproject.mapper.GameMapper;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameScheduleService {

    private final GameRepository gameRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final GameMapper gameMapper;

    public GameScheduleService(GameRepository gameRepository, CrewMemberRepository crewMemberRepository,
            GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.crewMemberRepository = crewMemberRepository;
        this.gameMapper = gameMapper;
    }

    public List<GameDto> findAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }

    public void initializeSchedule() {
        // Optional: Initialize default entries or log action
    }

    @Transactional
    public GameDto addGame(GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto, new HashSet<>());
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    @Transactional
    public void assignCrewToGame(Long gameId, List<Long> crewMemberIds) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with id: " + gameId));

        Set<CrewMember> crew = new HashSet<>(crewMemberRepository.findAllById(crewMemberIds));
        game.setAssignedCrew(crew);
        gameRepository.save(game);
    }

    @Transactional
    public List<GameDto> createGameSchedule(List<GameDto> games) {
        return games.stream()
                .map(gameDto -> {
                    Game game = gameMapper.toEntity(gameDto, new HashSet<>());
                    Game savedGame = gameRepository.save(game);
                    return gameMapper.toDto(savedGame);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GameDto assignCrew(Long gameId, List<Long> crewMemberIds) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Set<CrewMember> crewMembers = new HashSet<>(crewMemberRepository.findAllById(crewMemberIds));
        game.setAssignedCrew(crewMembers);

        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

}
