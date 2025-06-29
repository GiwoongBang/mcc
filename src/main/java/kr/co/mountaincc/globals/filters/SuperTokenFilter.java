package kr.co.mountaincc.globals.filters;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.maps.dtos.userDtos.UserDto;
import kr.co.mountaincc.users.jwt.MccJwtUtil;
import kr.co.mountaincc.users.oauth2.CustomOAuth2UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SuperTokenFilter extends OncePerRequestFilter {

    private final MccJwtUtil mccJwtUtil;

    private static final String ACCESS_CATEGORY = "access";

    private static final String BEARER_PREFIX = "BEARER_";

    private static final String SUPER_TOKEN_COOKIE = "Super-Token";

    private static final String TOKEN_STATUS_HEADER = "Token-Status";

    private static final String STATUS_EXPIRED = "EXPIRED";

    private static final String STATUS_INVALID = "INVALID";

    private static final String REQUIRED_ROLE = "ROLE_ADMIN";

    public SuperTokenFilter(MccJwtUtil mccJwtUtil) {
        this.mccJwtUtil = mccJwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (SUPER_TOKEN_COOKIE.equals(cookie.getName())) {
                    token = cookie.getValue();

                    break;
                }
            }
        }

        if (token == null) {
            token = request.getHeader(SUPER_TOKEN_COOKIE);
        }


        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }

        if (token != null) {
            try {
                mccJwtUtil.isExpired(token);

                String category = mccJwtUtil.getCategory(token);
                String role = mccJwtUtil.getRole(token);

                if (ACCESS_CATEGORY.equals(category) && REQUIRED_ROLE.equals(role)) {
                    String username = mccJwtUtil.getUsername(token);
                    String nickname = mccJwtUtil.getNickname(token);
                    String profileImg = mccJwtUtil.getProfileImg(token);

                    UserDto userDto = UserDto.builder()
                            .username(username)
                            .role(role)
                            .nickname(nickname)
                            .profileImg(profileImg)
                            .build();

                    CustomOAuth2UserEntity principal = new CustomOAuth2UserEntity(userDto);
                    Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (ExpiredJwtException e) {
                response.setHeader(TOKEN_STATUS_HEADER, STATUS_EXPIRED);
            } catch (Exception e) {
                response.setHeader(TOKEN_STATUS_HEADER, STATUS_INVALID);
            }
        }

        filterChain.doFilter(request, response);
    }
}