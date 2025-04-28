package edu.tcu.cs.frogcrewproject.repository;

import edu.tcu.cs.frogcrewproject.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
