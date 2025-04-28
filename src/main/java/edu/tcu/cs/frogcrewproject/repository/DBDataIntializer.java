package edu.tcu.cs.frogcrewproject.repository;

import edu.tcu.cs.frogcrewproject.entity.Availability;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.entity.Game;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
@Profile("!test")
public class DBDataIntializer {

    @Bean
    CommandLineRunner initDatabase(CrewMemberRepository crewMemberRepository,
            GameRepository gameRepository,
            AvailabilityRepository availabilityRepository) {
        return args -> {
            // Create sample crew members
            CrewMember john = new CrewMember();
            john.setFirstName("John");
            john.setLastName("Doe");
            john.setEmail("john.doe@tcu.edu");
            john.setPhoneNumber("555-0101");
            john.setRole("Referee");
            john.setQualifiedPosition("Head Referee");
            john.setInvited(true);

            CrewMember jane = new CrewMember();
            jane.setFirstName("Jane");
            jane.setLastName("Smith");
            jane.setEmail("jane.smith@tcu.edu");
            jane.setPhoneNumber("555-0102");
            jane.setRole("Line Judge");
            jane.setQualifiedPosition("Assistant Referee");
            jane.setInvited(true);

            CrewMember mike = new CrewMember();
            mike.setFirstName("Mike");
            mike.setLastName("Johnson");
            mike.setEmail("mike.johnson@tcu.edu");
            mike.setPhoneNumber("555-0103");
            mike.setRole("Scorekeeper");
            mike.setQualifiedPosition("Official Scorekeeper");
            mike.setInvited(true);

            crewMemberRepository.saveAll(Arrays.asList(john, jane, mike));

            // Create sample games
            Game game1 = new Game();
            game1.setOpponent("Baylor");
            game1.setSport("Basketball");
            game1.setVenue("Ed & Rae Schollmaier Arena");
            game1.setGameDateTime(LocalDateTime.now().plusDays(7));
            game1.setAssignedCrew(new HashSet<>(Arrays.asList(john, jane)));

            Game game2 = new Game();
            game2.setOpponent("Oklahoma");
            game2.setSport("Football");
            game2.setVenue("Amon G. Carter Stadium");
            game2.setGameDateTime(LocalDateTime.now().plusDays(14));
            game2.setAssignedCrew(new HashSet<>(Arrays.asList(mike, john)));

            gameRepository.saveAll(Arrays.asList(game1, game2));

            // Create sample availabilities
            Availability availability1 = new Availability();
            availability1.setCrewMember(john);
            availability1.setGame(game1);
            availability1.setAvailable(true);
            availability1.setComment("Available for the entire game");

            Availability availability2 = new Availability();
            availability2.setCrewMember(jane);
            availability2.setGame(game1);
            availability2.setAvailable(true);
            availability2.setComment("Can work first half only");

            Availability availability3 = new Availability();
            availability3.setCrewMember(mike);
            availability3.setGame(game2);
            availability3.setAvailable(false);
            availability3.setComment("Out of town for family event");

            availabilityRepository.saveAll(Arrays.asList(availability1, availability2, availability3));
        };
    }
}
