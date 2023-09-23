package ewm_stat_service.model;

import dto.EndpointHitDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HitMapper {

    public EndpointHitDto toHitDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public EndpointHit toHit(EndpointHitDto hitDto) {
        return new EndpointHit(
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }
}
