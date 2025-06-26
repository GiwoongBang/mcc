package kr.co.mountaincc.users.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.maps.dtos.userDtos.UserDto;
import kr.co.mountaincc.users.oauth2.CustomOAuth2UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final MccJwtUtil mccJwtUtil;

    public JwtFilter(MccJwtUtil mccJwtUtil) {
        this.mccJwtUtil = mccJwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("BEARER_")) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        authorization = cookie.getValue();

                        break;
                    }
                }
            }
        }

        if (authorization == null || !authorization.startsWith("BEARER_")) {
            response.setHeader("Token-Status", "MISSING");
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = authorization.substring(7);
        try {
            mccJwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setHeader("Token-Status", "EXPIRED");
            filterChain.doFilter(request, response);

            return;
        }

        String category = mccJwtUtil.getCategory(accessToken);
        if (category == null || !category.equals("access")) {
            filterChain.doFilter(request, response);

            return;
        }

        String username = mccJwtUtil.getUsername(accessToken);
        String role = mccJwtUtil.getRole(accessToken);
        String nickname = mccJwtUtil.getNickname(accessToken);
        String profileImg = mccJwtUtil.getProfileImg(accessToken);

        UserDto userDto = UserDto.builder()
                .username(username)
                .role(role)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();

        CustomOAuth2UserEntity customOAuth2User = new CustomOAuth2UserEntity(userDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}