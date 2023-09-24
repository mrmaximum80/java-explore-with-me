package ewmstatservice.controller;

import dto.EndpointHitDto;
import dto.ViewStats;
import ewmstatservice.model.EndpointHit;
import ewmstatservice.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "")
public class StatsController {

    private final StatService statService;

    @Autowired
    public StatsController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody EndpointHitDto hitDto) {
        return statService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam(name = "end")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(name = "uris", required = false) List<String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        return statService.getStats(start, end, uris, unique);
    }

}
