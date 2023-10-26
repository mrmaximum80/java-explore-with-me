package ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.main.category.dto.CategoryDto;
import ewm.main.user.dto.UserShortDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventShortDto {

    private Long id;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Long confirmedRequests;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private boolean paid;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private long views;

}
