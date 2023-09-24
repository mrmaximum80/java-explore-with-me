package ewmstatservice.service;

import dto.EndpointHitDto;
import dto.ViewStats;
import ewmstatservice.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHit saveHit(EndpointHitDto hitDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
