package ewm_stat_service.service;

import dto.EndpointHitDto;
import dto.ViewStats;
import ewm_stat_service.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHit saveHit(EndpointHitDto hitDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
