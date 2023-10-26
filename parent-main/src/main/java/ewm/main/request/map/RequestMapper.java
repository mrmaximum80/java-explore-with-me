package ewm.main.request.map;

import ewm.main.request.dto.ParticipationRequestDto;
import ewm.main.request.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );
    }
}
