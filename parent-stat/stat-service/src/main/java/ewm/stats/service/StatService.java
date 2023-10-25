package ewm.stats.service;

import ewm.dto.EndpointHitDto;
import ewm.dto.ViewStats;
import ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHit saveHit(EndpointHitDto hitDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
