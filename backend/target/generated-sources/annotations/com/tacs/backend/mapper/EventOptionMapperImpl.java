package com.tacs.backend.mapper;

import com.tacs.backend.dto.EventOptionDto;
import com.tacs.backend.model.EventOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-25T12:20:37-0300",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230511-1142, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class EventOptionMapperImpl implements EventOptionMapper {

    @Override
    public EventOptionDto entityToDto(EventOption eventOption) {
        if ( eventOption == null ) {
            return null;
        }

        EventOptionDto.EventOptionDtoBuilder eventOptionDto = EventOptionDto.builder();

        eventOptionDto.dateTime( eventOption.getDateTime() );
        eventOptionDto.eventName( eventOption.getEventName() );
        eventOptionDto.id( eventOption.getId() );
        eventOptionDto.updateDate( eventOption.getUpdateDate() );
        eventOptionDto.voteQuantity( eventOption.getVoteQuantity() );

        return eventOptionDto.build();
    }

    @Override
    public EventOption dtoToEntity(EventOptionDto eventOptionDto) {
        if ( eventOptionDto == null ) {
            return null;
        }

        EventOption.EventOptionBuilder eventOption = EventOption.builder();

        eventOption.dateTime( eventOptionDto.getDateTime() );
        eventOption.eventName( eventOptionDto.getEventName() );
        eventOption.id( eventOptionDto.getId() );
        eventOption.updateDate( eventOptionDto.getUpdateDate() );
        eventOption.voteQuantity( eventOptionDto.getVoteQuantity() );

        return eventOption.build();
    }

    @Override
    public Set<EventOptionDto> entitySetToDtoSet(Set<EventOption> eventOptions) {
        if ( eventOptions == null ) {
            return null;
        }

        Set<EventOptionDto> set = new LinkedHashSet<EventOptionDto>( Math.max( (int) ( eventOptions.size() / .75f ) + 1, 16 ) );
        for ( EventOption eventOption : eventOptions ) {
            set.add( entityToDto( eventOption ) );
        }

        return set;
    }

    @Override
    public Set<EventOption> dtoSetToEntitySet(Set<EventOptionDto> eventOptionsDto) {
        if ( eventOptionsDto == null ) {
            return null;
        }

        Set<EventOption> set = new LinkedHashSet<EventOption>( Math.max( (int) ( eventOptionsDto.size() / .75f ) + 1, 16 ) );
        for ( EventOptionDto eventOptionDto : eventOptionsDto ) {
            set.add( dtoToEntity( eventOptionDto ) );
        }

        return set;
    }

    @Override
    public List<EventOptionDto> entityListToDtoList(List<EventOption> eventOptions) {
        if ( eventOptions == null ) {
            return null;
        }

        List<EventOptionDto> list = new ArrayList<EventOptionDto>( eventOptions.size() );
        for ( EventOption eventOption : eventOptions ) {
            list.add( entityToDto( eventOption ) );
        }

        return list;
    }
}
