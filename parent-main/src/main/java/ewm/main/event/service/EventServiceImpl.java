package ewm.main.event.service;

import ewm.dto.EndpointHitDto;
import ewm.dto.ViewStats;
import ewm.main.category.model.Category;
import ewm.main.category.repository.CategoryRepository;
import ewm.main.error.ConflictException;
import ewm.main.error.NotChangeableEventException;
import ewm.main.error.NotFoundException;
import ewm.main.error.WrongDateException;
import ewm.main.event.controller.AdminParam;
import ewm.main.event.controller.PublicParam;
import ewm.main.event.controller.Sort;
import ewm.main.event.dto.*;
import ewm.main.event.map.EventMapper;
import ewm.main.event.model.Event;
import ewm.main.event.model.Location;
import ewm.main.event.model.State;
import ewm.main.event.model.StateAction;
import ewm.main.event.repository.EventRepository;
import ewm.main.event.repository.LocationRepository;
import ewm.main.request.dto.ParticipationRequestDto;
import ewm.main.request.map.RequestMapper;
import ewm.main.request.model.ParticipationRequest;
import ewm.main.request.model.Status;
import ewm.main.request.repository.RequestRepository;
import ewm.main.user.model.User;
import ewm.main.user.repository.UserRepository;
import ewm.stats.client.HitClient;
import ewm.stats.client.StatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final HitClient hitClient;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto newEvent, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newEvent.getCategory() + " was not found"));
        if (newEvent.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new WrongDateException("Field: eventDate. Error: должно быть не раньше, чем через 2 часа от " +
                    "текущего времени");
        }
        Location location = locationRepository.save(newEvent.getLocation());
        Event event = EventMapper.toEvent(newEvent, user, category, location);
        event = eventRepository.save(event);
        log.info("New event created with id={}, title={}.", event.getId(), event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        int start;
        int end = from + size;
        List<Event> events = eventRepository.findAllByInitiatorId(userId);
        log.info("Got user's with id={} events.", userId);
        if (from < events.size()) {
            start = from;
        } else return Collections.emptyList();
        if (end >= events.size()) {
            end = events.size();
        }
        events = events.subList(start, end);
        List<String> uris = events.stream().map(event -> "events/" + event.getId()).collect(Collectors.toList());
        ResponseEntity<Object> viewStats = statsClient
                .getStats(LocalDateTime.of(1000, 01, 01, 00, 0, 0),
                        LocalDateTime.of(3050, 01, 01, 00, 0, 0), uris, true);
        List<ViewStats> views = (List<ViewStats>) viewStats.getBody();

        List<EventShortDto> eventShortDtos = events.stream().map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        for (EventShortDto efd : eventShortDtos) {
            String uri = "events/" + efd.getId();
            for (ViewStats v : views) {
                if (v.getUri().equals(uri)) {
                    efd.setViews(v.getHits());
                    views.remove(v);
                    break;
                }
            }
        }

        return eventShortDtos;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new WrongDateException("Field: eventDate. Error: должно быть не раньше, чем через 2 часа от " +
                    "текущего времени");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new NotChangeableEventException("Only pending or canceled events can be changed");
        }

        updateEventFields(updateEvent, event);

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
        event = eventRepository.save(event);
        log.info("Event {} updated by user {}", eventId, userId);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        List<ParticipationRequest> participationRequests = requestRepository.findAllByEvent_id(eventId);
        log.info("Requests of event {} was found.", eventId);
        return participationRequests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new WrongDateException("Field: eventDate. Error: должно быть не раньше, чем через 1 час от " +
                    "текущего времени");
        }

        updateEventFields(EventMapper.toUpdateEventUserRequest(updateEvent), event);
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    log.info("Event {} published by admin", eventId);
                } else {
                    throw new ConflictException("Cannot publish the event because it's not in the right state: "
                            + event.getState());
                }
            }
            if (updateEvent.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (!event.getState().equals(State.PUBLISHED)) {
                    event.setState(State.REJECTED);
                    log.info("Event {} canceled by admin", eventId);
                } else {
                    throw new ConflictException("Cannot cancel the event " + eventId + " because it's PUBLISHED!");
                }
            }
        }
        event = eventRepository.save(event);
        log.info("Event {} updated by admin", eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (Boolean.TRUE.equals(!event.getRequestModeration()) || event.getParticipantLimit() == 0) {
            EventRequestStatusUpdateResult result = getEventRequestStatusUpdateResult(eventId);
            return result;
        }
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Event has reached the limit of participation requests");
        }
        Status status = updateRequest.getStatus();
        List<ParticipationRequest> participationRequests = requestRepository
                .findAllByEvent_idAndIdIn(eventId, updateRequest.getRequestIds());

        if (status.equals(Status.REJECTED)) {
            for (int i = 0; i < participationRequests.size(); i++) {
                ParticipationRequest request = participationRequests.get(i);
                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.REJECTED);
                    requestRepository.save(request);
                }
            }
        }

        if (status.equals(Status.CONFIRMED)) {
            for (int i = 0; i < participationRequests.size(); i++) {
                ParticipationRequest request = participationRequests.get(i);
                if (request.getStatus().equals(Status.PENDING)) {
                    request.setStatus(Status.CONFIRMED);
                    requestRepository.save(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                        for (int j = i + 1; j < participationRequests.size(); j++) {
                            request = participationRequests.get(j);
                            if (request.getStatus().equals(Status.PENDING)) {
                                request.setStatus(Status.REJECTED);
                                requestRepository.save(request);
                            }
                        }
                        break;
                    }
                }
            }
        }
        eventRepository.save(event);
        EventRequestStatusUpdateResult result = getEventRequestStatusUpdateResult(eventId);
        return result;

    }

    @Override
    public List<EventFullDto> adminGetEvents(AdminParam param) {
        List<Event> events = eventRepository.findAllByAdminParam(param);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByAny(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        List<String> uris = new ArrayList<>();
        uris.add("start");
        uris.add("event/" + eventId);
        uris.add("end");
        ResponseEntity<Object> viewStats = statsClient
                .getStats(LocalDateTime.of(1000, 01, 01, 00, 0, 0),
                        LocalDateTime.of(3050, 01, 01, 00, 0, 0), uris, true);
        List<Map<String, Object>> views = (List<Map<String, Object>>) viewStats.getBody();
        if (views.isEmpty()) {
            event.setViews(0L);
        } else {
            Integer hits = (Integer) views.get(0).get("hits");
            event.setViews(Long.parseLong(String.valueOf(hits)));
        }
        eventRepository.save(event);
        String ip = request.getRemoteAddr();
        EndpointHitDto endpointHit = new EndpointHitDto(
                "ewm",
                "event/" + eventId,
                ip,
                LocalDateTime.now().withNano(0)
        );
        hitClient.postEndpoint(endpointHit);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> publicGetEvents(PublicParam param, HttpServletRequest request) {

        List<Event> events = eventRepository.findAllByPublicParam(param);

        List<String> uris = events.stream().map(e -> "event/" + e.getId()).collect(Collectors.toList());

        ResponseEntity<Object> viewStats = statsClient
                .getStatsNoUnique(LocalDateTime.of(1000, 01, 01, 00, 0, 0),
                        LocalDateTime.of(3050, 01, 01, 00, 0, 0), uris);
        List<ViewStats> views = (List<ViewStats>) viewStats.getBody();

        for (Event event : events) {
            String uri = "events/" + event.getId();
            for (ViewStats v : views) {
                if (v.getUri().equals(uri)) {
                    event.setViews(v.getHits());
                    views.remove(v);
                    break;
                }
            }
        }

        if (param.getSort() != null) {
            if (param.getSort().equals(Sort.EVENT_DATE)) {
                events = events.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            }
            if (param.getSort().equals(Sort.VIEWS)) {
                events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }

        int start;
        int end = param.getFrom() + param.getSize();

        if (param.getFrom() < events.size()) {
            start = param.getFrom();
        } else return Collections.emptyList();
        if (end >= events.size()) {
            end = events.size();
        }
        events = events.subList(start, end);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private EventRequestStatusUpdateResult getEventRequestStatusUpdateResult(Long eventId) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = requestRepository
                .findAllByEvent_idAndStatus(eventId, Status.CONFIRMED).stream().map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        result.setConfirmedRequests(confirmedRequests);
        List<ParticipationRequestDto> rejectedRequests = requestRepository
                .findAllByEvent_idAndStatus(eventId, Status.REJECTED).stream().map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    private void updateEventFields(UpdateEventUserRequest updateEvent, Event event) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id=" + updateEvent.getCategory() + " was not found"));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
            if (date.isBefore(LocalDateTime.now())) {
                throw new WrongDateException("Event date cannot be in past.");
            }
            event.setEventDate(date);
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}
