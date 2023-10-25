package ewm.main.user.dto;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class NewUserRequest {

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
