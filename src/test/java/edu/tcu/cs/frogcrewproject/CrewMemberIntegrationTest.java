package edu.tcu.cs.frogcrewproject;

import edu.tcu.cs.frogcrewproject.dto.CrewMemberDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CrewMemberIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @BeforeEach
    void setup() {
        crewMemberRepository.deleteAll();
    }

    @Test
    void testCreateAndGetCrewMember() {
        CrewMemberDto dto = new CrewMemberDto();
        dto.setFirstName("John");
        dto.setLastName("Smith");
        dto.setEmail("john@example.com");
        dto.setPhoneNumber("321-654-9870");
        dto.setRole("Director");
        dto.setQualifiedPosition("Camera");
        dto.setInvited(false);

        // Log the DTO before sending
        System.out.println("Sending DTO: " + dto);

        ResponseEntity<CrewMemberDto> postResponse = restTemplate.postForEntity("/api/crew-members", dto,
                CrewMemberDto.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode(), "POST request should succeed");
        assertNotNull(postResponse.getBody(), "Response body should not be null");

        // Log the response
        System.out.println("POST Response: " + postResponse.getBody());

        assertNotNull(postResponse.getBody().getId(), "ID should not be null");
        assertEquals("John", postResponse.getBody().getFirstName(), "First name should be 'John' after creation");
        assertEquals("Smith", postResponse.getBody().getLastName(), "Last name should be 'Smith' after creation");

        Long id = postResponse.getBody().getId();
        ResponseEntity<CrewMemberDto> getResponse = restTemplate.getForEntity("/api/crew-members/" + id,
                CrewMemberDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "GET request should succeed");
        assertNotNull(getResponse.getBody(), "Response body should not be null");

        // Log the response
        System.out.println("GET Response: " + getResponse.getBody());

        assertNotNull(getResponse.getBody().getFirstName(), "First name should not be null");
        assertEquals("John", getResponse.getBody().getFirstName(), "First name should be 'John' after retrieval");
    }

    @Test
    void testGetAllCrewMembers() {
        CrewMember crew1 = new CrewMember();
        crew1.setFirstName("Alice");
        crew1.setLastName("Walker");
        crew1.setEmail("alice@example.com");
        crew1.setPhoneNumber("555-111-2222");
        crew1.setRole("Producer");
        crew1.setQualifiedPosition("Graphics");
        crew1.setInvited(false);

        CrewMember crew2 = new CrewMember();
        crew2.setFirstName("Bob");
        crew2.setLastName("Marley");
        crew2.setEmail("bob@example.com");
        crew2.setPhoneNumber("555-222-3333");
        crew2.setRole("Audio");
        crew2.setQualifiedPosition("Mic");
        crew2.setInvited(false);

        List<CrewMember> savedMembers = crewMemberRepository.saveAll(List.of(crew1, crew2));
        assertNotNull(savedMembers);
        assertEquals(2, savedMembers.size());

        ResponseEntity<CrewMemberDto[]> response = restTemplate.getForEntity("/api/crew-members",
                CrewMemberDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testDeleteCrewMember() {
        CrewMember member = new CrewMember();
        member.setFirstName("Delete");
        member.setLastName("Me");
        member.setEmail("delete@example.com");
        member.setPhoneNumber("999-888-7777");
        member.setRole("TD");
        member.setQualifiedPosition("Bug");
        member.setInvited(false);

        CrewMember saved = crewMemberRepository.save(member);
        assertNotNull(saved);
        assertNotNull(saved.getId());

        restTemplate.delete("/api/crew-members/" + saved.getId());
        assertFalse(crewMemberRepository.findById(saved.getId()).isPresent());
    }
}
