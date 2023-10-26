package ewm.main.event.repository;

import ewm.main.event.controller.AdminParam;
import ewm.main.event.controller.PublicParam;
import ewm.main.event.model.Event;

import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findAllByAdminParam(AdminParam param);

    List<Event> findAllByPublicParam(PublicParam param);
}
