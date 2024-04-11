package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import t.meshgroup.models.requests.JwtAuthenticationResponse;
import t.meshgroup.models.requests.SignInRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        Long userId = userService.getUserIdByEmail(email);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userId.toString(),
                password
        ));

        userService.userDetailsService().loadUserByUsername(userId.toString());

        String jwt = jwtService.generateJwtToken(userId);
        log.info(String.format("User %d successfully signed in.", userId));
        return new JwtAuthenticationResponse(jwt);
    }
}
