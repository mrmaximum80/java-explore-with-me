package ewm.stats.client;

import ewm.stats.client.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    private static final String API_PREFIX = "/stats";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, boolean unique) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "uris", uris,
                "unique", unique
        );
        return get("?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStatsNoParam(LocalDateTime start, LocalDateTime end) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr
        );
        return get("?start={start}&end={end}", parameters);
    }

    public ResponseEntity<Object> getStatsNoUris(LocalDateTime start, LocalDateTime end, boolean unique) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "unique", unique
        );
        return get("?start={start}&end={end}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStatsNoUnique(LocalDateTime start, LocalDateTime end,
                                                   List<String> uris) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "uris", uris
        );
        return get("?start={start}&end={end}&uris={uris}", parameters);
    }



}
