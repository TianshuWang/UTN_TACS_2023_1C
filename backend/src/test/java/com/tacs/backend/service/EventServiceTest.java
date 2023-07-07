package com.tacs.backend.service;

import com.tacs.backend.dto.EventDto;
import com.tacs.backend.dto.EventOptionDto;
import com.tacs.backend.dto.UserDto;
import com.tacs.backend.exception.EntityNotFoundException;
import com.tacs.backend.exception.EventStatusException;
import com.tacs.backend.exception.UserException;
import com.tacs.backend.exception.UserIsNotOwnerException;
import com.tacs.backend.mapper.EventMapper;
import com.tacs.backend.mapper.EventOptionMapper;
import com.tacs.backend.model.Event;
import com.tacs.backend.model.EventOption;
import com.tacs.backend.model.Role;
import com.tacs.backend.model.User;
import com.tacs.backend.repository.EventOptionRepository;
import com.tacs.backend.repository.EventRepository;
import com.tacs.backend.repository.UserRepository;
import com.tacs.backend.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;
  @Mock
  private EventOptionRepository eventOptionRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private EventMapper eventMapper;
  @Mock
  private EventOptionMapper eventOptionMapper;

  @InjectMocks
  private EventService eventService;

  private EventDto eventDto;
  private EventOptionDto eventOptionDto;
  private Set<EventOptionDto> eventOptionDtoSet;
  private EventOption eventOption;
  private Event event;
  private UserDto userDto;
  private List<User> users;
  private User user;
  private static MockedStatic<Utils> utils;

  @BeforeAll
  public static void init() {
    utils = Mockito.mockStatic(Utils.class);
  }
  @BeforeEach
  void setup() {
    user = User.builder()
        .id("idididiidid")
        .firstName("Facundo")
        .lastName("Perez")
        .username("123Perez")
        .password("UnaContraseña198!")
        .role(Role.USER)
        .build();
    users = Collections.singletonList(user);
    userDto = UserDto.builder()
            .id("idididiidid0")
            .firstName("Raul")
            .lastName("Flores")
            .build();

    eventOptionDto = EventOptionDto.builder()
        .id("idididididid2")
        .dateTime(Date.valueOf(LocalDate.now().plusDays(3)))
        .voteQuantity(0)
        .build();
    eventOptionDtoSet = Set.of(eventOptionDto);
    eventOption = EventOption.builder()
        .id("idididididid2")
        .dateTime(Date.valueOf(LocalDate.now().plusDays(3)))
        .voteQuantity(0)
        .updateDate(Date.valueOf(LocalDate.now()))
        .build();
    eventDto = EventDto.builder()
        .id("idididididid4")
        .name("unEvento")
        .description("descripcion")
        .status("VOTE_PENDING")
        .eventOptions(eventOptionDtoSet)
        .ownerUser(userDto)
        .registeredUsers(Set.of())
        .build();
    event = Event.builder()
        .id("idididididid4")
        .name("unEvento")
        .description("descripcion")
        .ownerUser(user)
        .status(Event.Status.VOTE_PENDING)
        .eventOptions(null)
        .registeredUsers(new HashSet<>())
        .createDate(Date.valueOf(LocalDate.now()))
        .build();
  }

  @Test
  @DisplayName("Should be created correctly an event")
  void createEventTest(){
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));
    utils.when(Utils::getCurrentUsername).thenReturn("username");

    assertDoesNotThrow(() -> eventService.createEvent(eventDto));
    Mockito.verify(userRepository).findByUsername(Mockito.any());
    Mockito.verify(eventMapper).entityToDto(Mockito.any());
    Mockito.verify(eventOptionMapper).dtoSetToEntitySet(eventDto.getEventOptions());
    Mockito.verify(eventOptionRepository).saveAll(Mockito.any());
    Mockito.verify(eventRepository).save(Mockito.any());
  }

  @Test
  @DisplayName("Should throw exception when Event does not exist")
  void getEventTest(){
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> eventService.getEventById("ididid"));
    Mockito.verify(eventRepository).findById("ididid");
  }

  @Test
  @DisplayName("Should get an existing event correctly")
  void getEventByIdTest(){
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));
    assertDoesNotThrow(() -> eventService.getEventById("ididid"));
    Mockito.verify(eventRepository).findById("ididid");
    Mockito.verify(eventMapper).entityToDto(event);
  }

  @Test
  @DisplayName("Should throw exception when the User is already registered for the Event")
  void registerEventTest(){
    event.setRegisteredUsers(Set.of(user));
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

    utils.when(Utils::getCurrentUsername).thenReturn("username");
    assertThrows(UserException.class, () -> eventService.registerEvent("ididid"));
    Mockito.verify(eventRepository).findById("ididid");
    Mockito.verify(userRepository).findByUsername(Mockito.any());
  }

  @Test
  @DisplayName("Should register the user correctly to the event")
  void registerEventTest2(){
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));
    assertTrue(event.getRegisteredUsers().isEmpty());

    utils.when(Utils::getCurrentUsername).thenReturn("username");
    assertDoesNotThrow(() -> eventService.registerEvent("ididid"));

    assertEquals(1, (long) event.getRegisteredUsers().size());
    Mockito.verify(eventRepository).findById("ididid");
    Mockito.verify(userRepository).findByUsername(Mockito.any());
    Mockito.verify(eventMapper).entityToDto(Mockito.any());
    Mockito.verify(eventRepository).save(Mockito.any());
  }

  @Test
  @DisplayName("Should throw exception when the User tries to close the vote and is not the Owner.")
  void closeEventVoteTest(){
    event.setOwnerUser(user);
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));
    User user1 = User.builder().username("username").build();
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user1));
    assertThrows(UserIsNotOwnerException.class, () -> eventService.changeEventStatus("ididid", Event.Status.VOTE_CLOSED.name()));

    Mockito.verify(eventRepository).findById("ididid");
    Mockito.verify(userRepository).findByUsername(Mockito.any());
  }

  @Test
  @DisplayName("Should close the vote correctly")
  void closeEventVoteTest2(){
    event.setOwnerUser(user);
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

    utils.when(Utils::getCurrentUsername).thenReturn("username");
    assertDoesNotThrow(() -> eventService.changeEventStatus("ididid", Event.Status.VOTE_CLOSED.name()));

    Mockito.verify(eventRepository).findById("ididid");
    Mockito.verify(userRepository).findByUsername(Mockito.any());
    Mockito.verify(eventRepository).save(event);
    Mockito.verify(eventMapper).entityToDto(Mockito.any());
    assertEquals(Event.Status.VOTE_CLOSED, event.getStatus());
  }

  @Test
  @DisplayName("Should throw exception when User try to vote but the voting is already closed")
  void voteEventOptionTest2(){
    Mockito.when(eventOptionRepository.findById("ididid")).thenReturn(Optional.of(eventOption));
    event.setStatus(Event.Status.VOTE_CLOSED);
    Mockito.when(eventRepository.findById("ididid")).thenReturn(Optional.of(event));

    utils.when(Utils::getCurrentUsername).thenReturn("username");
    assertThrows(EventStatusException.class,
          () -> eventService.voteEventOption("ididid", "ididid"));

    Mockito.verify(eventOptionRepository).findById("ididid");
    Mockito.verify(eventRepository).findById("ididid");
  }

}
