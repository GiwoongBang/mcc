package kr.co.mountaincc.maps.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_map_recommend_course_common")
@Entity
public class RecommendCourseCommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_title", nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private double distance;

    @Column(name = "elevation_gain")
    private double elevationGain;

    @Column(name = "thumbnail_image")
    private String thumbnailImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recommendCourseCommon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendCourseDetailEntity> details;

    @Builder
    public RecommendCourseCommonEntity(String title, String description, double distance, double elevationGain,
                                       String thumbnailImg, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.description = description;
        this.distance = distance;
        this.elevationGain = elevationGain;
        this.thumbnailImg = thumbnailImg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RecommendCourseCommonEntity of(String title, String description,
                                                 double distance, double elevationGain, String thumbnailImg) {

        return RecommendCourseCommonEntity.builder()
                .title(title)
                .description(description)
                .distance(distance)
                .elevationGain(elevationGain)
                .thumbnailImg(thumbnailImg)
                .details(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
