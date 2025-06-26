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
                if ("Super-Token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            token = request.getHeader("Super-Token");
        }


        if (token != null && token.startsWith("BEARER_")) {
            token = token.substring(7);
        }

        if (token != null) {
            try {
                mccJwtUtil.isExpired(token);

                String category = mccJwtUtil.getCategory(token);
                String role = mccJwtUtil.getRole(token);

                if ("access".equals(category) && "ROLE_ADMIN".equals(role)) {
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
                response.setHeader("Token-Status", "EXPIRED");
            } catch (Exception e) {
                response.setHeader("Token-Status", "INVALID");
            }
        }

        filterChain.doFilter(request, response);
    }
}