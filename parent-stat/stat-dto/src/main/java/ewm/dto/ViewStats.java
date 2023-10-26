package ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ViewStats {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotNull
    private Long hits;

}
