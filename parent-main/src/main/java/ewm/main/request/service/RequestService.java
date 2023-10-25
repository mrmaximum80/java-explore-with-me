package ewm.main.request.service;

import ewm.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);


    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
