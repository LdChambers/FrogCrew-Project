package edu.tcu.cs.frogcrewproject;

import edu.tcu.cs.frogcrewproject.dto.CrewMemberDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CrewMemberIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String jwtToken;

    @BeforeEach
    void setup() {
        crewMemberRepository.deleteAll();

        // Create a UserDetails object with both USER and ADMIN roles
        UserDetails userDetails = new User(
                "testuser",
                "password",
                Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")));

        // Create authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        // Generate JWT token
        jwtToken = jwtTokenProvider.createToken(authentication);
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return headers;
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

        HttpEntity<CrewMemberDto> request = new HttpEntity<>(dto, getAuthHeaders());
        ResponseEntity<CrewMemberDto> postResponse = restTemplate.exchange(
                "/api/crew-members",
                HttpMethod.POST,
                request,
                CrewMemberDto.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode(), "POST request should succeed");
        assertNotNull(postResponse.getBody(), "Response body should not be null");
        assertNotNull(postResponse.getBody().getId(), "ID should not be null");
        assertEquals("John", postResponse.getBody().getFirstName(), "First name should be 'John' after creation");
        assertEquals("Smith", postResponse.getBody().getLastName(), "Last name should be 'Smith' after creation");

        Long id = postResponse.getBody().getId();
        HttpEntity<Void> getRequest = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<CrewMemberDto> getResponse = restTemplate.exchange(
                "/api/crew-members/" + id,
                HttpMethod.GET,
                getRequest,
                CrewMemberDto.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "GET request should succeed");
        assertNotNull(getResponse.getBody(), "Response body should not be null");
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

        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<CrewMemberDto[]> response = restTemplate.exchange(
                "/api/crew-members",
                HttpMethod.GET,
                request,
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

        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders());
        restTemplate.exchange(
                "/api/crew-members/" + saved.getId(),
                HttpMethod.DELETE,
                request,
                Void.class);

        assertFalse(crewMemberRepository.findById(saved.getId()).isPresent());
    }
}
