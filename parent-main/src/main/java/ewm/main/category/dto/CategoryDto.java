package ewm.main.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
