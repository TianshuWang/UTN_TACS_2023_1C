package com.tacs.backend.mapper;

import com.tacs.backend.dto.EventDto;
import com.tacs.backend.dto.EventOptionDto;
import com.tacs.backend.dto.UserDto;
import com.tacs.backend.model.Event;
import com.tacs.backend.model.EventOption;
import com.tacs.backend.model.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-25T12:20:37-0300",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230511-1142, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto entityToDto(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDto.EventDtoBuilder eventDto = EventDto.builder();

        eventDto.createDate( event.getCreateDate() );
        eventDto.description( event.getDescription() );
        eventDto.eventOptions( eventOptionSetToEventOptionDtoSet( event.getEventOptions() ) );
        eventDto.id( event.getId() );
        eventDto.name( event.getName() );
        eventDto.ownerUser( userToUserDto( event.getOwnerUser() ) );
        eventDto.registeredUsers( userSetToUserDtoSet( event.getRegisteredUsers() ) );
        if ( event.getStatus() != null ) {
            eventDto.status( event.getStatus().name() );
        }

        return eventDto.build();
    }

    @Override
    public Event dtoToEntity(EventDto eventDto) {
        if ( eventDto == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.createDate( eventDto.getCreateDate() );
        event.description( eventDto.getDescription() );
        event.eventOptions( eventOptionDtoSetToEventOptionSet( eventDto.getEventOptions() ) );
        event.id( eventDto.getId() );
        event.name( eventDto.getName() );
        event.ownerUser( userDtoToUser( eventDto.getOwnerUser() ) );
        event.registeredUsers( userDtoSetToUserSet( eventDto.getRegisteredUsers() ) );
        if ( eventDto.getStatus() != null ) {
            event.status( Enum.valueOf( Event.Status.class, eventDto.getStatus() ) );
        }

        return event.build();
    }

    @Override
    public Set<EventDto> entitySetToDtoSet(Set<Event> events) {
        if ( events == null ) {
            return null;
        }

        Set<EventDto> set = new LinkedHashSet<EventDto>( Math.max( (int) ( events.size() / .75f ) + 1, 16 ) );
        for ( Event event : events ) {
            set.add( entityToDto( event ) );
        }

        return set;
    }

    protected EventOptionDto eventOptionToEventOptionDto(EventOption eventOption) {
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

    protected Set<EventOptionDto> eventOptionSetToEventOptionDtoSet(Set<EventOption> set) {
        if ( set == null ) {
            return null;
        }

        Set<EventOptionDto> set1 = new LinkedHashSet<EventOptionDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( EventOption eventOption : set ) {
            set1.add( eventOptionToEventOptionDto( eventOption ) );
        }

        return set1;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.firstName( user.getFirstName() );
        userDto.id( user.getId() );
        userDto.lastName( user.getLastName() );
        userDto.username( user.getUsername() );

        return userDto.build();
    }

    protected Set<UserDto> userSetToUserDtoSet(Set<User> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserDto> set1 = new LinkedHashSet<UserDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( User user : set ) {
            set1.add( userToUserDto( user ) );
        }

        return set1;
    }

    protected EventOption eventOptionDtoToEventOption(EventOptionDto eventOptionDto) {
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

    protected Set<EventOption> eventOptionDtoSetToEventOptionSet(Set<EventOptionDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<EventOption> set1 = new LinkedHashSet<EventOption>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( EventOptionDto eventOptionDto : set ) {
            set1.add( eventOptionDtoToEventOption( eventOptionDto ) );
        }

        return set1;
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( userDto.getFirstName() );
        user.id( userDto.getId() );
        user.lastName( userDto.getLastName() );
        user.username( userDto.getUsername() );

        return user.build();
    }

    protected Set<User> userDtoSetToUserSet(Set<UserDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<User> set1 = new LinkedHashSet<User>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserDto userDto : set ) {
            set1.add( userDtoToUser( userDto ) );
        }

        return set1;
    }
}
