package com.tacs.backend.mapper;

import com.tacs.backend.dto.EventOptionDto;
import com.tacs.backend.model.EventOption;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventOptionMapper {

    EventOptionMapper INSTANCE = Mappers.getMapper(EventOptionMapper.class);

    EventOptionDto entityToDto(EventOption eventOption);

    EventOption dtoToEntity(EventOptionDto eventOptionDto);

    Set<EventOptionDto> entitySetToDtoSet(Set<EventOption> eventOptions);

    Set<EventOption> dtoSetToEntitySet(Set<EventOptionDto> eventOptionsDto);

    List<EventOptionDto> entityListToDtoList(List<EventOption> eventOptions);

}
