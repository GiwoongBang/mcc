package kr.co.mountaincc.maps.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.maps.dtos.userDtos.CheckAuthResponseDto;
import kr.co.mountaincc.maps.entities.UserEntity;
import kr.co.mountaincc.maps.repositories.UserRepository;
import kr.co.mountaincc.users.jwt.JwtUtil;
import kr.co.mountaincc.users.jwt.RefreshTokenRepository;
import kr.co.mountaincc.users.oauth2.CustomOAuth2UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFRESH_TOKEN = "Refresh-Token";

    private static final String ACCESS_CATEGORY = "access";

    private static final String REFRESH_CATEGORY = "refresh";

    private static final String BEARER_PREFIX = "BEARER_";

    public CustomOAuth2UserEntity getOAuth2UserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2UserEntity)) {

            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        CustomOAuth2UserEntity customUser = (CustomOAuth2UserEntity) authentication.getPrincipal();

        return customUser;
    }

    public ResponseEntity<CheckAuthResponseDto> checkAuthStatus(HttpServletRequest request) {
        String authorization = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION)) {
                    authorization = cookie.getValue();

                    break;
                }
            }
        }

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = authorization.substring(BEARER_PREFIX.length());
        try {
            if (jwtUtil.isExpired(accessToken)) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok(
                    new CheckAuthResponseDto(
                            jwtUtil.getUsername(accessToken),
                            jwtUtil.getNickname(accessToken),
                            jwtUtil.getRole(accessToken),
                            jwtUtil.getProfileImg(accessToken)
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader(REFRESH_TOKEN);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(REFRESH_TOKEN)) {
                        authorization = cookie.getValue();

                        break;
                    }
                }
            }
        }

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {

            return new ResponseEntity<>("Refresh Token is NULL", HttpStatus.BAD_REQUEST);
        }

        String refreshToken = authorization.substring(BEARER_PREFIX.length());
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity<>("Refresh Token is expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (category == null || !category.equals(REFRESH_CATEGORY)) {

            return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {

            return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String nickname = jwtUtil.getNickname(refreshToken);
        String profileImg = jwtUtil.getProfileImg(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(username, ACCESS_CATEGORY, role, nickname, profileImg);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, REFRESH_CATEGORY, role, nickname, profileImg);

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        Date newRefreshTokenExpiration = jwtUtil.getExpiration(newRefreshToken);
        jwtUtil.saveRefreshToken(username, newRefreshToken, newRefreshTokenExpiration);

        String accessHeader = jwtUtil.createSetCookieHeader(AUTHORIZATION, BEARER_PREFIX + newAccessToken, JWT_ACCESS_EXPIRATION_TIME / 1000);
        String refreshHeader = jwtUtil.createSetCookieHeader(REFRESH_TOKEN, BEARER_PREFIX + newRefreshToken, JWT_REFRESH_EXPIRATION_TIME / 1000);

        response.addHeader("Set-Cookie", accessHeader);
        response.addHeader("Set-Cookie", refreshHeader);

        return new ResponseEntity<>("CLEAR", HttpStatus.OK);
    }

    public String deleteUser(HttpServletResponse response) {
        CustomOAuth2UserEntity customUser = getOAuth2UserInfo();
        String username = customUser.getUsername();

        UserEntity foundUser = userRepository.findByUsername(username);
        if (foundUser == null) {

            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        refreshTokenRepository.deleteAllByUsername(username);
        userRepository.delete(foundUser);

        deleteCookie(response, AUTHORIZATION);
        deleteCookie(response, REFRESH_TOKEN);

        SecurityContextHolder.clearContext();

        return "User withdrawal completed: Username - " + foundUser.getUsername();
    }

    private void deleteCookie(HttpServletResponse response, String key) {

        String cookie = ResponseCookie.from(key, "")
                .domain("mountaincc.co.kr")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()
                .toString();

        response.addHeader("Set-Cookie", cookie);
    }

}
