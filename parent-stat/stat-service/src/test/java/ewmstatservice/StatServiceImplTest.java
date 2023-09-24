package ewmstatservice;

import dto.EndpointHitDto;
import dto.ViewStats;
import ewmstatservice.model.EndpointHit;
import ewmstatservice.service.StatService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatServiceImplTest {

    private final StatService statService;

    private static LocalDateTime dateTime;
    private static EndpointHitDto hit1;
    private static EndpointHitDto hit2;
    private static EndpointHitDto hit3;

    @BeforeAll
    static void before() {
        dateTime = LocalDateTime.now();
        hit1 = new EndpointHitDto("ewm", "event/1", "192.168.000.001", dateTime);
        hit2 = new EndpointHitDto("ewm", "event/1", "192.168.000.002", dateTime);
        hit3 = new EndpointHitDto("ewm", "event/2", "192.168.000.002", dateTime);
    }

    @Test
    void saveHitTest() {

        EndpointHit savedHit = statService.saveHit(hit1);

        assertEquals("ewm", savedHit.getApp());
        assertEquals("event/1", savedHit.getUri());
        assertEquals("192.168.000.001", savedHit.getIp());
        assertEquals(dateTime, savedHit.getTimestamp());
    }

    @Test
    void getStatsTest() {

        statService.saveHit(hit1);
        statService.saveHit(hit1);
        statService.saveHit(hit2);
        statService.saveHit(hit3);

        List<String> uris = new ArrayList<>();
        uris.add("event/1");

        List<ViewStats> viewStats = statService.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), uris, true);

        assertEquals(viewStats.size(), 1);
        assertEquals("event/1", viewStats.get(0).getUri());
        assertEquals(2, viewStats.get(0).getHits());

        viewStats = statService.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), uris, false);

        assertEquals(viewStats.size(), 1);
        assertEquals("event/1", viewStats.get(0).getUri());
        assertEquals(3, viewStats.get(0).getHits());

        viewStats = statService.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), null, false);

        assertEquals(viewStats.size(), 2);
        assertEquals("event/1", viewStats.get(0).getUri());
        assertEquals(3, viewStats.get(0).getHits());

        viewStats = statService.getStats(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), null, true);

        assertEquals(viewStats.size(), 2);
        assertEquals("event/1", viewStats.get(0).getUri());
        assertEquals(2, viewStats.get(0).getHits());

    }
}
