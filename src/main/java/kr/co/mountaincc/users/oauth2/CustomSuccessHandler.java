package kr.co.mountaincc.users.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.users.jwt.MccJwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MccJwtUtil mccJwtUtil;

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFRESH_TOKEN = "Refresh-Token";

    private static final String ACCESS_CATEGORY = "access";

    private static final String REFRESH_CATEGORY = "refresh";

    private static final String BEARER_PREFIX = "BEARER_";

    private static final String REDIRECT_URL = "https://www.mountaincc.co.kr";

    public CustomSuccessHandler(MccJwtUtil mccJwtUtil) {
        this.mccJwtUtil = mccJwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2UserEntity customUserDetails = (CustomOAuth2UserEntity) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String role = getRole(authentication);
        String nickname = customUserDetails.getName();
        String profileImg = customUserDetails.getProfileImg();

        String accessToken = mccJwtUtil.generateAccessToken(username, ACCESS_CATEGORY, role, nickname, profileImg);
        String refreshToken = mccJwtUtil.generateRefreshToken(username, REFRESH_CATEGORY, role, nickname, profileImg);

        Date refreshTokenExpiration = mccJwtUtil.getExpiration(refreshToken);
        mccJwtUtil.saveRefreshToken(username, refreshToken, refreshTokenExpiration);

        String accessHeader = mccJwtUtil.createSetCookieHeader(AUTHORIZATION, BEARER_PREFIX + accessToken, JWT_ACCESS_EXPIRATION_TIME / 1000);
        String refreshHeader = mccJwtUtil.createSetCookieHeader(REFRESH_TOKEN, BEARER_PREFIX + refreshToken, JWT_REFRESH_EXPIRATION_TIME / 1000);

        response.addHeader("Set-Cookie", accessHeader);
        response.addHeader("Set-Cookie", refreshHeader);

        response.sendRedirect(REDIRECT_URL);
    }

    private static String getRole(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        return role;
    }

}
