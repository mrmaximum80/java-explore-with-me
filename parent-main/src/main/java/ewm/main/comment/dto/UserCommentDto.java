package ewm.main.comment.dto;

import ewm.main.event.dto.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserCommentDto {
    @NotBlank
    private String text;

    @NotNull
    private EventShortDto event;

    @NotNull
    private LocalDateTime created;
}
