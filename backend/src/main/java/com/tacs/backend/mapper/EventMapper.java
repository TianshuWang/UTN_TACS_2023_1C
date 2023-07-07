package com.tacs.backend.mapper;

import com.tacs.backend.dto.EventDto;
import com.tacs.backend.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto entityToDto(Event event);

    Event dtoToEntity(EventDto eventDto);

    Set<EventDto> entitySetToDtoSet(Set<Event> events);
}