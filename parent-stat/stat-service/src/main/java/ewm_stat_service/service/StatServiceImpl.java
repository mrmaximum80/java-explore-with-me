package ewm_stat_service.service;

import dto.EndpointHitDto;
import dto.ViewStats;
import ewm_stat_service.model.EndpointHit;
import ewm_stat_service.model.HitMapper;
import ewm_stat_service.repository.HitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatServiceImpl implements StatService {

    private final HitRepository hitRepository;

    @Autowired
    public StatServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public EndpointHit saveHit(EndpointHitDto hitDto) {
        log.info("Save hit with app={} and uri={}", hitDto.getApp(), hitDto.getUri());
        return hitRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null) {
            if (unique) {
                log.info("Get unique stats of start={}, end={}", start, end);
                return hitRepository.getViewStatsUnique(start, end);
            } else {
                log.info("Get stats of start={}, end={}", start, end);
                return hitRepository.getViewStats(start, end);
            }
        } else {
            if (unique) {
                log.info("Get unique stats of start={}, end={}, uri={}", start, end, uris);
                return hitRepository.getViewStatsUnique(start, end, uris);
            } else {
                log.info("Get stats of start={}, end={}, uri={}", start, end, uris);
                return hitRepository.getViewStats(start, end, uris);
            }
        }
    }
}