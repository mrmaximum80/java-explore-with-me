package ewm.main.event.dto;

import ewm.main.request.dto.ParticipationRequestDto;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}
