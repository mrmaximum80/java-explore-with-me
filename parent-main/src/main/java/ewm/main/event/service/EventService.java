package ewm.main.event.service;

import ewm.main.event.controller.AdminParam;
import ewm.main.event.controller.PublicParam;
import ewm.main.event.dto.*;
import ewm.main.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEvent, Long userId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEvent);

    EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest updateRequest);

    List<EventFullDto> adminGetEvents(AdminParam param);

    EventFullDto getEventByAny(Long eventId, HttpServletRequest request);

    List<EventShortDto> publicGetEvents(PublicParam param, HttpServletRequest request);
}
