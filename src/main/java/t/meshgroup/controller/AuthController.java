package t.meshgroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import t.meshgroup.models.requests.JwtAuthenticationResponse;
import t.meshgroup.models.requests.SignInRequest;
import t.meshgroup.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth user requests")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Auth user")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authService.signIn(request);
    }

    /**
     * For Delete in next version.
     *
     * @since 0.0.1v
     **/
    @Deprecated
    @Operation(summary = "Hello world function!")
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
}
