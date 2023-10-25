package ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.main.event.model.Location;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

}
