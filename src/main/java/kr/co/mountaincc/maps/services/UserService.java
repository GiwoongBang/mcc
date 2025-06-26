package kr.co.mountaincc.maps.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.maps.dtos.userDtos.CheckAuthResponseDto;
import kr.co.mountaincc.maps.entities.UserEntity;
import kr.co.mountaincc.maps.repositories.UserRepository;
import kr.co.mountaincc.users.jwt.MccJwtUtil;
import kr.co.mountaincc.users.jwt.RefreshTokenRepository;
import kr.co.mountaincc.users.oauth2.CustomOAuth2UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    private final MccJwtUtil mccJwtUtil;

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

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
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        if (authorization == null || !authorization.startsWith("BEARER_")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = authorization.substring(7);
        try {
            if (mccJwtUtil.isExpired(accessToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok(
                    new CheckAuthResponseDto(
                            mccJwtUtil.getUsername(accessToken),
                            mccJwtUtil.getNickname(accessToken),
                            mccJwtUtil.getRole(accessToken),
                            mccJwtUtil.getProfileImg(accessToken)
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Refresh-Token");

        if (authorization == null || !authorization.startsWith("BEARER_")) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Refresh-Token")) {
                        authorization = cookie.getValue();

                        break;
                    }
                }
            }
        }

        if (authorization == null | !authorization.startsWith("BEARER_")) {

            return new ResponseEntity<>("Refresh Token is NULL", HttpStatus.BAD_REQUEST);
        }

        String refreshToken = authorization.substring(7);
        try {
            mccJwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity<>("Refresh Token is expired", HttpStatus.BAD_REQUEST);
        }

        String category = mccJwtUtil.getCategory(refreshToken);
        if (category == null || !category.equals("refresh")) {

            return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);

        String username = mccJwtUtil.getUsername(refreshToken);
        String role = mccJwtUtil.getRole(refreshToken);
        String nickname = mccJwtUtil.getNickname(refreshToken);
        String profileImg = mccJwtUtil.getProfileImg(refreshToken);

        String newAccessToken = mccJwtUtil.generateAccessToken(username, "access", role, nickname, profileImg);
        String newRefreshToken = mccJwtUtil.generateRefreshToken(username, "refresh", role, nickname, profileImg);

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        Date newRefreshTokenExpiration = mccJwtUtil.getExpiration(newRefreshToken);
        mccJwtUtil.saveRefreshToken(username, newRefreshToken, newRefreshTokenExpiration);

        String accessHeader = mccJwtUtil.createSetCookieHeader("Authorization", "BEARER_" + newAccessToken, JWT_ACCESS_EXPIRATION_TIME / 1000);
        String refreshHeader = mccJwtUtil.createSetCookieHeader("Refresh-Token", "BEARER_" + newRefreshToken, JWT_REFRESH_EXPIRATION_TIME / 1000);

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

        deleteCookie(response, "Authorization");
        deleteCookie(response, "Refresh-Token");

        SecurityContextHolder.clearContext();

        return "User withdrawal completed: Username - " + foundUser.getUsername();
    }

    private void deleteCookie(HttpServletResponse response, String key) {
        String cookie = key + "=;" +
                " Path=/;" +
                " Max-Age=0;" +
                " HttpOnly;" +
                " Secure;" +
                " SameSite=None";

        response.addHeader("Set-Cookie", cookie);
    }

}
