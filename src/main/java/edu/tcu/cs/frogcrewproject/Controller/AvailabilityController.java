package edu.tcu.cs.frogcrewproject.Controller;

import edu.tcu.cs.frogcrewproject.dto.AvailabilityDto;
import edu.tcu.cs.frogcrewproject.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // UC-7: Crew Member Submits Availability
    @PostMapping("/submit")
    public ResponseEntity<String> submitAvailability(@RequestBody AvailabilityDto dto) {
        availabilityService.submitAvailability(dto);
        return ResponseEntity.ok("Availability submitted successfully.");
    }
}

