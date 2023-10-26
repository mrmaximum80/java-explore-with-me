package ewm.main.event.controller;

import ewm.main.event.dto.EventFullDto;
import ewm.main.event.dto.UpdateEventAdminRequest;
import ewm.main.event.model.State;
import ewm.main.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> adminGetEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                  @RequestParam(name = "states", required = false) List<State> states,
                                  @RequestParam(name = "categories", required = false) List<Long> categories,
                                  @RequestParam(name = "rangeStart", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(name = "rangeEnd", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @RequestParam(name = "size", defaultValue = "10") Integer size
                                  ) {
        AdminParam param = new AdminParam(users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.adminGetEvents(param);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest updateEvent) {
        return eventService.adminUpdateEvent(eventId, updateEvent);
    }
}
