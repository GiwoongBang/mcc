package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mountaincc.maps.dtos.userDtos.CheckAuthResponseDto;
import kr.co.mountaincc.maps.services.UserService;
import kr.co.mountaincc.users.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "User API 입니다.")
@RequestMapping("/mcc/users")
@RestController
public class UserController {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Value("${spring.jwt.super.secret}")
    private String superSecret;

    private static final String BEARER_PREFIX = "BEARER_";

    private static final String SUPER_TOKEN_COOKIE = "Super-Token";

    private static final int SUPER_TOKEN_EXPIRATION_SECONDS = 60 * 60 * 12; // 12시간

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Tag(name = "User API")
    @Operation(summary = "로그인 여부 확인", description = "쿠키에 담긴 Authorization 토큰으로 로그인 여부 확인")
    @GetMapping("/check-auth")
    public ResponseEntity<CheckAuthResponseDto> checkAuth(HttpServletRequest request) {

        return userService.checkAuthStatus(request);
    }

    @Tag(name = "User API")
    @Operation(summary = "Authorization 재발급",
            description = "Refresh-Token 으로 만료된 Authorization 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        return userService.reissueToken(request, response);
    }

    @Tag(name = "User API")
    @Operation(summary = "슈퍼 토큰 생성",
            description = "secretKey를 검증 후, 12시간짜리 슈퍼 토큰을 생성하여 헤더에 반환")
    @GetMapping("/super-token")
    public ResponseEntity<String> generateSuperToken(@RequestParam("secretKey") String secretKey,
                                                     HttpServletResponse response) {
        if (!superSecret.equalsIgnoreCase(secretKey)) {

            return new ResponseEntity<>("Invalid secretKey", HttpStatus.UNAUTHORIZED);
        }

        String superToken = jwtUtil.generateSuperToken();
        String fullToken = BEARER_PREFIX + superToken;

        String superHeader = jwtUtil.createSetCookieHeader(SUPER_TOKEN_COOKIE, fullToken, SUPER_TOKEN_EXPIRATION_SECONDS);

        response.setHeader(HttpHeaders.SET_COOKIE, superHeader);
        response.setHeader(SUPER_TOKEN_COOKIE, fullToken);

        return new ResponseEntity<>("SUPER TOKEN GENERATED", HttpStatus.OK);
    }

    @Tag(name = "User API")
    @Operation(summary = "회원 탈퇴",
            description = "로그인한 사용자의 회원 정보를 삭제합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(HttpServletResponse response) {
        try {
            String resData = userService.deleteUser(response);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User withdrawal failed: " + e.getMessage());
        }
    }

}