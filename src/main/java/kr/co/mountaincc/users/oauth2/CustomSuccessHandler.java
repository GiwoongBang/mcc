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

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

    private final MccJwtUtil mccJwtUtil;

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

        String accessToken = mccJwtUtil.generateAccessToken(username, "access", role, nickname, profileImg);
        String refreshToken = mccJwtUtil.generateRefreshToken(username, "refresh", role, nickname, profileImg);

        Date refreshTokenExpiration = mccJwtUtil.getExpiration(refreshToken);
        mccJwtUtil.saveRefreshToken(username, refreshToken, refreshTokenExpiration);

        String accessHeader = mccJwtUtil.createSetCookieHeader("Authorization", "BEARER_" + accessToken, JWT_ACCESS_EXPIRATION_TIME / 1000);
        String refreshHeader = mccJwtUtil.createSetCookieHeader("Refresh-Token", "BEARER_" + refreshToken, JWT_REFRESH_EXPIRATION_TIME / 1000);

        response.addHeader("Set-Cookie", accessHeader);
        response.addHeader("Set-Cookie", refreshHeader);

        response.sendRedirect("https://www.mountaincc.co.kr");
    }

    private static String getRole(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        return role;
    }

}
