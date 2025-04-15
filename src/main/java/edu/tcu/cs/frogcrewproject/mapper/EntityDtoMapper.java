package edu.tcu.cs.frogcrewproject.mapper;

import edu.tcu.cs.frogcrewproject.dto.CrewMemberDto;
import edu.tcu.cs.frogcrewproject.entity.CrewMember;
import org.springframework.stereotype.Component;

public interface EntityDtoMapper {
    CrewMemberDto toDto(CrewMember entity);

    CrewMember toEntity(CrewMemberDto dto);
}

@Component
class EntityDtoMapperImpl implements EntityDtoMapper {
    @Override
    public CrewMemberDto toDto(CrewMember entity) {
        CrewMemberDto dto = new CrewMemberDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setRole(entity.getRole());
        dto.setQualifiedPosition(entity.getQualifiedPosition());
        dto.setInvited(entity.isInvited());
        return dto;
    }

    @Override
    public CrewMember toEntity(CrewMemberDto dto) {
        CrewMember entity = new CrewMember();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole());
        entity.setQualifiedPosition(dto.getQualifiedPosition());
        entity.setInvited(dto.isInvited());
        return entity;
    }
}
