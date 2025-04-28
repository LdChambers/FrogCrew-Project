package edu.tcu.cs.frogcrewproject.dto;

import lombok.Data;

@Data
public class AvailabilityDto {

    private Long id;
    private Long crewMemberId;
    private Long gameId;
    private boolean available;
    private String comment;

    public Long getCrewMemberId() {
        return crewMemberId;
    }

    public void setCrewMemberId(Long crewMemberId) {
        this.crewMemberId = crewMemberId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
