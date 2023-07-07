package com.tacs.backend.service;

import com.tacs.backend.dto.EventDto;
import com.tacs.backend.exception.*;
import com.tacs.backend.mapper.EventMapper;
import com.tacs.backend.mapper.EventOptionMapper;
import com.tacs.backend.model.Event;
import com.tacs.backend.model.EventOption;
import com.tacs.backend.model.User;
import com.tacs.backend.repository.EventOptionRepository;
import com.tacs.backend.repository.EventRepository;
import com.tacs.backend.repository.UserRepository;
import com.tacs.backend.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventOptionRepository eventOptionRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final EventOptionMapper eventOptionMapper;

    public EventDto createEvent(EventDto request) {
        User currentUser = userRepository.findByUsername(Utils.getCurrentUsername()).orElseThrow();
        request.getEventOptions().forEach(options -> options.setEventName(request.getName()));
        Set<EventOption> eventOptionSet = eventOptionMapper.dtoSetToEntitySet(request.getEventOptions());
        Set<EventOption> savedEventOptionSet = Set.copyOf(eventOptionRepository.saveAll(eventOptionSet));

        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(Event.Status.VOTE_PENDING)
                .ownerUser(currentUser)
                .registeredUsers(new HashSet<>())
                .createDate(new Date())
                .eventOptions(savedEventOptionSet).build();

        return eventMapper.entityToDto(this.eventRepository.save(event));
    }

    public EventDto getEventById(String id) {
        Event event = getEvent(id);
        return eventMapper.entityToDto(event);
    }

    public Set<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll().stream()
                .sorted(Comparator.comparing(Event::getCreateDate)).toList();
        return eventMapper.entitySetToDtoSet(Set.copyOf(events));
    }

    public EventDto registerEvent(String id) {
        Event event = getEvent(id);
        User user = userRepository.findByUsername(Utils.getCurrentUsername()).orElseThrow();
        if(event.getRegisteredUsers().contains(user)) {
            throw new UserException(String.format("User: %s already registered to the event", user.getUsername()));
        }
        event.getRegisteredUsers().add(user);

        return eventMapper.entityToDto(eventRepository.save(event));
    }

    public EventDto changeEventStatus(String id, String status) {
        Event event = getEvent(id);
        User user = userRepository.findByUsername(Utils.getCurrentUsername()).orElseThrow();
        if(!event.getOwnerUser().getUsername().equals(user.getUsername())) {
            throw new UserIsNotOwnerException(String.format("User: %s is not allowed to change the status of event", user.getUsername()));
        }

        if (status.equals(event.getStatus().name())) {
            throw new EventStatusException("The event already has the status : " + status);
        }

        var state = Event.Status.valueOf(StringUtils.upperCase(status.trim()));
        event.setStatus(state);
        return eventMapper.entityToDto(eventRepository.save(event));
    }

    public EventDto voteEventOption(String idEvent, String idEventOption) {
        Event event = getEvent(idEvent);
        EventOption eventOption = eventOptionRepository.findById(idEventOption).orElseThrow(
                () -> new EntityNotFoundException("The event option is not found")
        );

        if (Event.Status.VOTE_CLOSED == event.getStatus()) {
            throw new EventStatusException("The event's vote has already closed, no more allowed to vote the event");
        }

        eventOption.setVoteQuantity(eventOption.getVoteQuantity() + 1);
        eventOption.setUpdateDate(new Date());
        eventOptionRepository.save(eventOption);
        return eventMapper.entityToDto(eventRepository.findById(idEvent).orElseThrow());
    }

    private Event getEvent(String id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("The event is not found")
        );
    }
}
