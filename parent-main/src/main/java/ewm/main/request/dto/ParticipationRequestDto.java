package ewm.main.request.dto;

import ewm.main.request.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    @NotNull
    private LocalDateTime created;

    @NotNull
    private Long event;

    @NotNull
    private Long requester;

    @NotNull
    private Status status;
}
