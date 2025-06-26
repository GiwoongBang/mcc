package kr.co.mountaincc.maps.entities;

import jakarta.persistence.*;
import kr.co.mountaincc.maps.dtos.userDtos.OAuth2Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImg;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomCourseCommonEntity> customCourses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseEntity> exercises;

    @Builder
    public UserEntity(String username, OAuth2Response oAuth2Response) {
        this.username = username;
        this.role = "ROLE_USER";
        this.nickname = oAuth2Response.getNickname();
        this.profileImg = oAuth2Response.getProfileImage();
        this.createdAt = LocalDateTime.now();
    }

    public void update(OAuth2Response oAuth2Response) {
        this.nickname = oAuth2Response.getNickname();
        this.profileImg = oAuth2Response.getProfileImage();
    }

}
