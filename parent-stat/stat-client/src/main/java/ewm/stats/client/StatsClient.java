package ewm.stats.client;

import ewm.dto.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {

    private static final String API_PREFIX = "/stats";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected final RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end,
                                    List<String> uris, boolean unique) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);

        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "uris", uris,
                "unique", unique);

        ResponseEntity<List<ViewStats>> response = null;
        try {
            response = rest
                    .exchange("?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET,
                            null, new ParameterizedTypeReference<>() {}, parameters);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot get ViewStats.");
        }
        List<ViewStats> viewStatsList = response.getBody();
        return viewStatsList;
    }

    public List<ViewStats> getStatsNoParam(LocalDateTime start, LocalDateTime end) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr);

        ResponseEntity<List<ViewStats>> response = null;
        try {
            response = rest
                    .exchange("?start={start}&end={end}", HttpMethod.GET,
                            null, new ParameterizedTypeReference<>() {
                            }, parameters);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot get ViewStats.");
        }
        List<ViewStats> viewStatsList = response.getBody();
        return viewStatsList;
    }

    public List<ViewStats> getStatsNoUris(LocalDateTime start, LocalDateTime end,
                                    boolean unique) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "unique", unique);

        ResponseEntity<List<ViewStats>> response = null;
        try {
            response = rest
                    .exchange("?start={start}&end={end}&unique={unique}", HttpMethod.GET,
                            null, new ParameterizedTypeReference<>() {
                            }, parameters);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot get ViewStats.");
        }
        List<ViewStats> viewStatsList = response.getBody();
        return viewStatsList;
    }

    public List<ViewStats> getStatsNoUnique(LocalDateTime start, LocalDateTime end,
                                    List<String> uris) {
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        Map<String, Object> parameters = Map.of(
                "start", startStr,
                "end", endStr,
                "uris", uris);

        ResponseEntity<List<ViewStats>> response = null;
        try {
            response = rest
                    .exchange("?start={start}&end={end}&uris={uris}}", HttpMethod.GET,
                            null, new ParameterizedTypeReference<>() {
                            }, parameters);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot get ViewStats.");
        }
        List<ViewStats> viewStatsList = response.getBody();
        return viewStatsList;
    }

}
