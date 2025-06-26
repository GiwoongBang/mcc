package kr.co.mountaincc.maps.dtos.mapDtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.ExerciseEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Schema(description = "운동 기록 응답")
@Getter
public class ExerciseResponseDto {

    @Schema(description = "운동 기록 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 ID", example = "100")
    private Long userId;

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "운동 제목", example = "북한산 등반")
    private String title;

    @Schema(description = "운동 시작 시간", example = "2024-03-01T08:00:00")
    private LocalDateTime startTime;

    @Schema(description = "운동 종료 시간", example = "2024-03-01T12:00:00")
    private LocalDateTime endTime;

    @Schema(description = "GPX 파일 URL", example = "https://example.com/path/to/file.gpx")
    private String gpxUrl;

    @Schema(description = "운동 거리(km)", example = "12.5")
    private double distance;

    @Schema(description = "평균 속도(km/h)", example = "4.8")
    private double avgSpeed;

    @Schema(description = "총 상승 고도(m)", example = "540.2")
    private double elevationGain;

    @Schema(description = "총 하강 고도(m)", example = "550.1")
    private double elevationLoss;

    @Schema(description = "이동 시간 (hh:mm:ss)", example = "03:30:00")
    private String movingTime;

    @Schema(description = "휴식 시간 (hh:mm:ss)", example = "00:30:00")
    private String restTime;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private ExerciseResponseDto(Long id, Long userId, SubCategoryMinimalResponseDto subCategory,
                                String title, LocalDateTime startTime, LocalDateTime endTime,
                                String gpxUrl, double distance, double avgSpeed, double elevationGain, double elevationLoss,
                                String movingTime, String restTime, String message) {
        this.id = id;
        this.userId = userId;
        this.subCategory = subCategory;
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
        this.message = message;
    }

    public static ExerciseResponseDto of(ExerciseEntity exerciseEntity, Long userId, String message) {

        return ExerciseResponseDto.builder()
                .id(exerciseEntity.getId())
                .userId(userId)
                .subCategory(SubCategoryMinimalResponseDto.of(exerciseEntity.getSubCategory()))
                .title(exerciseEntity.getTitle())
                .startTime(exerciseEntity.getStartTime())
                .endTime(exerciseEntity.getEndTime())
                .gpxUrl(exerciseEntity.getGpxUrl())
                .distance(exerciseEntity.getDistance())
                .avgSpeed(exerciseEntity.getAvgSpeed())
                .elevationGain(exerciseEntity.getElevationGain())
                .elevationLoss(exerciseEntity.getElevationLoss())
                .movingTime(formatDuration(exerciseEntity.getMovingTime()))
                .restTime(formatDuration(exerciseEntity.getRestTime()))
                .message(message)
                .build();
    }

    private static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    public static ExerciseResponseDto withError(String message) {

        return ExerciseResponseDto.builder()
                .message(message)
                .build();
    }

}
