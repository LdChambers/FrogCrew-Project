package edu.tcu.cs.frogcrewproject.repository;

import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    Optional<CrewMember> findByEmail(String email);
}

