package t.meshgroup.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import t.meshgroup.utils.auth.JwtUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final SecretKey jwtSigningKey = JwtUtils.getSignJwt();

    public String generateJwtToken(@NonNull Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ID", userId);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(jwtSigningKey, Jwts.SIG.HS512).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserId(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("USER_ID").toString();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(jwtSigningKey).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
