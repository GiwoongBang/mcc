package kr.co.mountaincc.users.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class MccJwtUtil {

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.super.expiration}")
    private int JWT_SUPER_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

    private final SecretKey secretKey;

    private final RefreshTokenRepository refreshTokenRepository;

    public MccJwtUtil(@Value("${spring.jwt.secret}") String secret,
                      RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getNickname(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getProfileImg(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("profileImg", String.class);
    }

    public Date getExpiration(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String generateAccessToken(String username, String category, String role, String nickname, String profileImg) {

        return Jwts.builder()
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("profileImg", profileImg)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_ACCESS_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username, String category, String role, String nickname, String profileImg) {

        return Jwts.builder()
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("profileImg", profileImg)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateSuperToken() {

        return Jwts.builder()
                .claim("username", "super")
                .claim("category", "access")
                .claim("role", "ROLE_ADMIN")
                .claim("nickname", "girin")
                .claim("profileImg", "https://images.pexels.com/photos/2541407/pexels-photo-2541407.jpeg")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_SUPER_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String createSetCookieHeader(String key, String value, int maxAge) {

        return ResponseCookie.from(key, value)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()
                .toString();
    }

    public void saveRefreshToken(String username, String refreshToken, Date expiration) {

        Boolean isExist = refreshTokenRepository.existsByUsername(username);
        if (isExist) refreshTokenRepository.deleteAllByUsername(username);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .username(username)
                .refreshToken(refreshToken)
                .expriration(expiration)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

}
