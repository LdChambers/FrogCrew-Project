package edu.tcu.cs.frogcrewproject;

import edu.tcu.cs.frogcrewproject.dto.CrewMemberDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import edu.tcu.cs.frogcrewproject.mapper.EntityDtoMapper;
import edu.tcu.cs.frogcrewproject.repository.CrewMemberRepository;
import edu.tcu.cs.frogcrewproject.service.CrewMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CrewMemberServiceTest {

    @Mock
    private CrewMemberRepository crewMemberRepository;

    @Mock
    private EntityDtoMapper mapper;

    @InjectMocks
    private CrewMemberService crewMemberService;

    private CrewMember member;
    private CrewMemberDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new CrewMember();
        member.setId(1L);
        member.setFirstName("Jane");
        member.setLastName("Doe");
        member.setEmail("jane@example.com");
        member.setPhoneNumber("123-456-7890");
        member.setRole("Producer");
        member.setQualifiedPosition("Camera");
        member.setInvited(false);

        dto = new CrewMemberDto();
        dto.setId(1L);
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setEmail("jane@example.com");
        dto.setPhoneNumber("123-456-7890");
        dto.setRole("Producer");
        dto.setQualifiedPosition("Camera");
        dto.setInvited(false);
    }

    @Test
    void testCreateCrewMember() {
        when(mapper.toEntity(dto)).thenReturn(member);
        when(crewMemberRepository.save(member)).thenReturn(member);
        when(mapper.toDto(member)).thenReturn(dto);

        CrewMemberDto saved = crewMemberService.create(dto);

        assertEquals("Jane", saved.getFirstName());
        verify(crewMemberRepository, times(1)).save(member);
    }

    @Test
    void testFindCrewMemberById() {
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(mapper.toDto(member)).thenReturn(dto);

        CrewMemberDto result = crewMemberService.findById(1L);

        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void testInviteCrewMember() {
        when(crewMemberRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(member));
        when(crewMemberRepository.save(any(CrewMember.class))).thenAnswer(invocation -> {
            CrewMember savedMember = invocation.getArgument(0);
            savedMember.setInvited(true);
            return savedMember;
        });

        crewMemberService.invite("jane@example.com");

        verify(crewMemberRepository).save(any(CrewMember.class));
        verify(crewMemberRepository).findByEmail("jane@example.com");
    }
}