package ewm.main.request.service;

import ewm.main.error.ConflictException;
import ewm.main.error.NotFoundException;
import ewm.main.event.model.Event;
import ewm.main.event.model.State;
import ewm.main.event.repository.EventRepository;
import ewm.main.request.dto.ParticipationRequestDto;
import ewm.main.request.map.RequestMapper;
import ewm.main.request.model.ParticipationRequest;
import ewm.main.request.model.Status;
import ewm.main.request.repository.RequestRepository;
import ewm.main.user.model.User;
import ewm.main.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        List<ParticipationRequest> participationRequests = requestRepository.findAllByRequester_id(userId);
        return participationRequests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getInitiator().getId().equals(userId)) {
            log.info("Initiator of the event cannot add a request to participate in his event");
            throw new ConflictException("Initiator of the event cannot add a request to participate in his event");
        }
        if (event.getState() != State.PUBLISHED) {
            log.info("Cannot participate in an unpublished event");
            throw new ConflictException("Cannot participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            log.info("Event has reached the limit of participation requests");
            throw new ConflictException("Event has reached the limit of participation requests");
        }
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setEvent(event);
        participationRequest.setRequester(user);
        participationRequest.setCreated(LocalDateTime.now().withNano(0));
        if (Boolean.FALSE.equals(event.getRequestModeration()) || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(Status.CONFIRMED);
            log.info("Request confirmed.");
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            log.info("Request added to event {}.", event.getId());
        } else {
            participationRequest.setStatus(Status.PENDING);
            log.info("Request created with panding status");
        }
        participationRequest = requestRepository.save(participationRequest);
        log.info("Request saved with id={}", participationRequest.getId());
        return RequestMapper.toRequestDto(participationRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        ParticipationRequest participationRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        participationRequest.setStatus(Status.CANCELED);
        participationRequest = requestRepository.save(participationRequest);
        return RequestMapper.toRequestDto(participationRequest);
    }

}
