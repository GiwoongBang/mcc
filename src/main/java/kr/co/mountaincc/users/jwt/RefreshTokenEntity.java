package kr.co.mountaincc.users.jwt;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Table(name = "mcc_user_refresh_token")
@Entity
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 600)
    private String refreshToken;

    @Column(nullable = false)
    private Date expriration;

    @Builder
    public RefreshTokenEntity(String username, String refreshToken, Date expriration) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expriration = expriration;
    }

}
