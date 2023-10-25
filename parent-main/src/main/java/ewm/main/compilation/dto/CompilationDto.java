package ewm.main.compilation.dto;

import ewm.main.event.dto.EventShortDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CompilationDto {

    private Long id;

    private List<EventShortDto> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
