package kr.co.mountaincc.users.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFRESH_TOKEN = "Refresh-Token";

    private static final String REFRESH_CATEGORY = "refresh";

    private static final String BEARER_PREFIX = "BEARER_";

    public CustomLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);

            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);

            return;
        }

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN)) {
                    authorization = cookie.getValue();

                    break;
                }
            }
        }

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        String refreshToken = authorization.substring(BEARER_PREFIX.length());
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (category == null || !category.equals(REFRESH_CATEGORY)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        deleteCookie(response, AUTHORIZATION);
        deleteCookie(response, REFRESH_TOKEN);

        SecurityContextHolder.clearContext();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = """
            {
              "status": "success",
              "message": "Logout completed. All tokens removed."
            }
            """;

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(body);
    }

    private void deleteCookie(HttpServletResponse response, String key) {
        String cookie = ResponseCookie.from(key, "")
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