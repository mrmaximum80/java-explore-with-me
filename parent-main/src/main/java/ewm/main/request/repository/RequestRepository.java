package ewm.main.request.repository;

import ewm.main.request.model.ParticipationRequest;
import ewm.main.request.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester_id(Long userId);

    List<ParticipationRequest> findAllByEvent_id(Long eventId);

    List<ParticipationRequest> findAllByEvent_idAndIdIn(Long eventId, List<Long> requestIds);

    List<ParticipationRequest> findAllByEvent_idAndStatus(Long eventId, Status status);
}
