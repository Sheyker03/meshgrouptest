package t.meshgroup.utils.auth;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

/**
 * Minimal protection against theft... We may dynamic create, encrypt and use...
 */
public final class JwtUtils {
    public final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static SecretKey getSignJwt() {
        String signKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3MTI1MDM4OTcsImV4cCI6MTc0NDAzOTg5NywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0UYkOiWlsUDpfXjcDTgHu0v1oGANSyywuV2rB8iR57Fw"; // todo for transfer to some store...
        byte[] keyBytes = Decoders.BASE64.decode(signKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
