package ewm.main.event.repository;

import ewm.main.error.WrongDateException;
import ewm.main.event.controller.AdminParam;
import ewm.main.event.controller.PublicParam;
import ewm.main.event.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Event> findAllByAdminParam(AdminParam param) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (param.getUsers() != null) {
            predicates.add(event.get("initiator").in(param.getUsers()));
        }
        if (param.getStates() != null) {
            predicates.add(event.get("state").in(param.getStates()));
        }

        if (param.getCategories() != null) {
            predicates.add(event.get("category").in(param.getCategories()));
        }

        LocalDateTime startDate;
        LocalDateTime endDate;
        if (param.getRangeStart() == null) {
            startDate = LocalDateTime.now();
        } else {
            startDate = param.getRangeStart();
        }
        if (param.getRangeEnd() == null) {
            endDate = LocalDateTime.of(3000, 01, 01, 0, 0, 0);
        } else {
            endDate = param.getRangeEnd();
        }
        predicates.add(cb.between(event.get("eventDate"), startDate, endDate));
        cq.where(predicates.toArray(new Predicate[0]));

        List<Event> events = em.createQuery(cq).getResultList();

        int start;
        int end = param.getFrom() + param.getSize();

        if (param.getFrom() < events.size()) {
            start = param.getFrom();
        } else return Collections.emptyList();
        if (end >= events.size()) {
            end = events.size();
        }
        events = events.subList(start, end);

        return events;
    }

    @Override
    public List<Event> findAllByPublicParam(PublicParam param) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (param.getText() != null) {
            Predicate annot = cb.like(cb.lower(event.get("annotation")), "%" + param.getText().toLowerCase() + "%");
            Predicate desc = cb.like(cb.lower(event.get("description")), "%" + param.getText().toLowerCase() + "%");
            predicates.add(cb.or(annot, desc));
        }
        if (param.getCategories() != null) {
            predicates.add(event.get("category").in(param.getCategories()));
        }
        if (param.getPaid() != null) {
            predicates.add(cb.equal(event.get("paid"), param.getPaid()));
        }

        LocalDateTime startDate;
        LocalDateTime endDate;
        if (param.getRangeStart() == null) {
            startDate = LocalDateTime.now();
        } else {
            startDate = param.getRangeStart();
        }
        if (param.getRangeEnd() == null) {
            endDate = LocalDateTime.of(3000, 01, 01, 0, 0, 0);
        } else {
            endDate = param.getRangeEnd();
        }
        if (startDate.isBefore(endDate)) {
            predicates.add(cb.between(event.get("eventDate"), startDate, endDate));
        } else {
            throw new WrongDateException("StartDate cannot be after end date.");
        }
        if (Boolean.TRUE.equals(param.getOnlyAvailable())) {
            predicates.add(cb.lt(event.get("confirmedRequests"), event.get("participantLimit"))
            );
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Event> events = em.createQuery(cq).getResultList();

        return events;
    }
}
