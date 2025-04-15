package edu.tcu.cs.frogcrewproject.mapper;

import edu.tcu.cs.frogcrewproject.dto.GameDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    public GameDto toDto(Game game) {
        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setOpponent(game.getOpponent());
        dto.setSport(game.getSport());
        dto.setVenue(game.getVenue());
        dto.setGameDateTime(game.getGameDateTime());
        dto.setAssignedCrewIds(
                game.getAssignedCrew()
                        .stream()
                        .map(CrewMember::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public Game toEntity(GameDto dto, Set<CrewMember> assignedCrew) {
        Game game = new Game();
        game.setId(dto.getId());
        game.setOpponent(dto.getOpponent());
        game.setSport(dto.getSport());
        game.setVenue(dto.getVenue());
        game.setGameDateTime(dto.getGameDateTime());
        game.setAssignedCrew(assignedCrew);
        return game;
    }
}
