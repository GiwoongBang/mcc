package kr.co.mountaincc.maps.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import jakarta.persistence.*;
import kr.co.mountaincc.maps.converters.DurationConverter;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.ExerciseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_map_exercise")
@Entity
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(name="title", nullable = false)
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="start_time", nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name="gpx_url", nullable = false)
    private String gpxUrl;

    @Column(name="distance", nullable = false)
    private double distance;

    @Column(name="avg_speed", nullable = false)
    private double avgSpeed;

    @Column(name="elevation_gain", nullable = false)
    private double elevationGain;

    @Column(name="elevation_loss", nullable = false)
    private double elevationLoss;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @Convert(converter = DurationConverter.class)
    @Column(name="moving_time", nullable = false)
    private Duration movingTime;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @Convert(converter = DurationConverter.class)
    @Column(name="rest_time", nullable = false)
    private Duration restTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Builder
    public ExerciseEntity(UserEntity userEntity, SubCategoryEntity subCategoryEntity,
                          String title, LocalDateTime startTime, LocalDateTime endTime,
                          String gpxUrl, double distance, double avgSpeed, double elevationGain, double elevationLoss,
                          Duration movingTime, Duration restTime, LocalDateTime createdAt) {
        this.user = userEntity;
        this.subCategory = subCategoryEntity;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gpxUrl = gpxUrl;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.elevationGain = elevationGain;
        this.elevationLoss = elevationLoss;
        this.movingTime = movingTime;
        this.restTime = restTime;
        this.createdAt = createdAt;
    }

    public static ExerciseEntity of(ExerciseRequestDto dto, UserEntity user, SubCategoryEntity subCategory) {

        return ExerciseEntity.builder()
                .user(user)
                .subCategory(subCategory)
                .title(dto.getTitle())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .gpxUrl(dto.getGpxUrl())
                .distance(dto.getDistance())
                .avgSpeed(dto.getAvgSpeed())
                .elevationGain(dto.getElevationGain())
                .elevationLoss(dto.getElevationLoss())
                .movingTime(parseDuration(dto.getMovingTime()))
                .restTime(parseDuration(dto.getRestTime()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static Duration parseDuration(String timeString) {
        return Duration.between(LocalTime.MIN, LocalTime.parse(timeString));
    }

}
