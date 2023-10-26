package ewm.main.event.dto;

import ewm.main.request.model.Status;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;
}
