package ewm.main.comment.dto;

import ewm.main.event.dto.EventShortDto;
import ewm.main.user.dto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private EventShortDto event;

    @NotNull
    private UserShortDto author;

    @NotNull
    private LocalDateTime created;
}
