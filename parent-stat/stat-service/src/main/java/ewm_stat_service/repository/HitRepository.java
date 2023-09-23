package ewm_stat_service.repository;

import dto.ViewStats;
import ewm_stat_service.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new dto.ViewStats(eh.app, eh.uri, count(eh.ip))" +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getViewStats(LocalDateTime start,
                                 LocalDateTime end
    );

    @Query("select new dto.ViewStats(eh.app, eh.uri, count(distinct eh.ip))" +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri " +
            "order by count(distinct eh.ip) desc")
    List<ViewStats> getViewStatsUnique(LocalDateTime start,
                                       LocalDateTime end
    );

    @Query("select new dto.ViewStats(eh.app, eh.uri, count(eh.ip))" +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and eh.uri in ?3 " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getViewStats(LocalDateTime start,
                                 LocalDateTime end,
                                 List<String> uris
    );

    @Query("select new dto.ViewStats(eh.app, eh.uri, count(distinct eh.ip))" +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and eh.uri in ?3 " +
            "group by eh.app, eh.uri " +
            "order by count(distinct eh.ip) desc")
    List<ViewStats> getViewStatsUnique(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris
    );
}
