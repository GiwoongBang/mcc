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

    private static final String AUTHORIZATION = "Authorization";

    private static final String BEARER_PREFIX = "BEARER_";

    private static final String ACCESS_CATEGORY = "access";

    private static final String TOKEN_STATUS_HEADER = "Token-Status";

    private static final String STATUS_MISSING = "MISSING";

    private static final String STATUS_EXPIRED = "EXPIRED";

    public JwtFilter(MccJwtUtil mccJwtUtil) {
        this.mccJwtUtil = mccJwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(AUTHORIZATION)) {
                        authorization = cookie.getValue();

                        break;
                    }
                }
            }
        }

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            response.setHeader(TOKEN_STATUS_HEADER, STATUS_MISSING);
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = authorization.substring(BEARER_PREFIX.length());
        try {
            mccJwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setHeader(TOKEN_STATUS_HEADER, STATUS_EXPIRED);
            filterChain.doFilter(request, response);

            return;
        }

        String category = mccJwtUtil.getCategory(accessToken);
        if (category == null || !category.equals(ACCESS_CATEGORY)) {
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