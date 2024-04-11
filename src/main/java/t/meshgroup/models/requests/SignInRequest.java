package t.meshgroup.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Request for auth")
public class SignInRequest {

    @Schema(name = "password", example = "Test724312ndfsgPass_1")
    @Size(min = 8, max = 500, message = "The password must be between 8 and 500 characters long")
    @NotBlank(message = "Password can't be is empty")
    private String password;

    @Schema(name = "email", example = "example@gmail.com")
    private String email;
}
