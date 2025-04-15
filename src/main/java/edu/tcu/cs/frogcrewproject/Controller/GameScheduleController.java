package edu.tcu.cs.frogcrewproject.Controller;

import edu.tcu.cs.frogcrewproject.dto.GameDto;
import edu.tcu.cs.frogcrewproject.service.GameScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameScheduleController {

    private final GameScheduleService service;

    public GameScheduleController(GameScheduleService service) {
        this.service = service;
    }

    // UC-5: Crew Member Views General Game Schedule
    @GetMapping
    public ResponseEntity<List<GameDto>> getAllGames() {
        return ResponseEntity.ok(service.findAllGames());
    }

    // UC-18: Admin Creates Game Schedule (creates multiple games)
    @PostMapping("/bulk")
    public ResponseEntity<List<GameDto>> createGameSchedule(@RequestBody List<GameDto> games) {
        return ResponseEntity.ok(service.createGameSchedule(games));
    }

    // UC-20: Admin Adds a Single Game to Game Schedule
    @PostMapping
    public ResponseEntity<GameDto> addGame(@RequestBody GameDto gameDto) {
        return ResponseEntity.ok(service.addGame(gameDto));
    }

    // UC-23: Admin Schedules Crew to a Game
    @PostMapping("/{gameId}/assign-crew")
    public ResponseEntity<GameDto> assignCrewToGame(@PathVariable Long gameId, @RequestBody List<Long> crewMemberIds) {
        return ResponseEntity.ok(service.assignCrew(gameId, crewMemberIds));
    }
}
