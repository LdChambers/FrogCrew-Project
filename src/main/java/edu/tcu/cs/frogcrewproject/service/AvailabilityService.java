package edu.tcu.cs.frogcrewproject.service;

import edu.tcu.cs.frogcrewproject.dto.AvailabilityDto;
import edu.tcu.cs.frogcrewproject.entity.Availability;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import edu.tcu.cs.frogcrewproject.repository.AvailabilityRepository;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final GameRepository gameRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
            CrewMemberRepository crewMemberRepository,
            GameRepository gameRepository) {
        this.availabilityRepository = availabilityRepository;
        this.crewMemberRepository = crewMemberRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public void submitAvailability(AvailabilityDto dto) {
        CrewMember crewMember = crewMemberRepository.findById(dto.getCrewMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Crew member not found."));

        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found."));

        Availability availability = new Availability();
        availability.setCrewMember(crewMember);
        availability.setGame(game);
        availability.setAvailable(dto.isAvailable());
        availability.setComment(dto.getComment());

        availabilityRepository.save(availability);
    }

    public List<AvailabilityDto> getAllAvailability() {
        return availabilityRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AvailabilityDto convertToDto(Availability availability) {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setId(availability.getId());
        dto.setCrewMemberId(availability.getCrewMember().getId());
        dto.setGameId(availability.getGame().getId());
        dto.setAvailable(availability.isAvailable());
        dto.setComment(availability.getComment());
        return dto;
    }
}
