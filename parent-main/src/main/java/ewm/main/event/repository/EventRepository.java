package ewm.main.event.repository;

import ewm.main.event.model.Event;
import ewm.main.event.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    List<Event> findAllByInitiatorId(Long userId);

    List<Event> findAllByInitiator_idInAndStateInAndCategory_idInAndEventDateBetween(
            List<Long> users, List<State> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd);

    Optional<Event> findByIdAndState(Long eventId, State state);
}

