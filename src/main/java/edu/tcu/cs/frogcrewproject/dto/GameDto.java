package edu.tcu.cs.frogcrewproject.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GameDto {
    private Long id;
    private String opponent;
    private String sport;
    private String venue;
    private LocalDateTime gameDateTime;
    private List<Long> assignedCrewIds; // Optional for UC-23

    public void setId(Long id) {
        this.id = id;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setGameDateTime(LocalDateTime gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    public void setAssignedCrewIds(List<Long> assignedCrewIds) {
        this.assignedCrewIds = assignedCrewIds;
    }
}
