package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExerciseRequestDto {

    @Schema(description = "운동한 장소의 SubCategory ID", example = "1")
    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @Schema(description = "운동 제목", example = "북한산 등반")
    @NotNull(message = "Title은 필수입니다.")
    private String title;

    @Schema(description = "운동 시작 시간", example = "2024-03-01T08:00:00")
    @NotNull(message = "Start Time은 필수입니다.")
    private LocalDateTime startTime;

    @Schema(description = "운동 종료 시간", example = "2024-03-01T12:00:00")
    @NotNull(message = "End Time은 필수입니다.")
    private LocalDateTime endTime;

    @Schema(description = "GPX 파일이 저장된 경로(URL)", example = "https://example.com/path/to/file.gpx")
    @NotNull(message = "GPX가 저장된 경로(URL)는 필수입니다.")
    private String gpxUrl;

    @Schema(description = "운동 거리(km)", example = "12.5")
    @NotNull(message = "Distance는 필수입니다.")
    private double distance;

    @Schema(description = "평균 속도(km/h)", example = "4.8")
    @NotNull(message = "Average Speed는 필수입니다.")
    private double avgSpeed;

    @Schema(description = "총 상승 고도(m)", example = "540.2")
    @NotNull(message = "Elevation Gain은 필수입니다.")
    private double elevationGain;

    @Schema(description = "총 하강 고도(m)", example = "550.1")
    @NotNull(message = "Elevation Loss는 필수입니다.")
    private double elevationLoss;

    @Schema(description = "이동 시간 (hh:mm:ss)", example = "03:30:00")
    @NotNull(message = "Moving Time은 필수입니다.")
    private String movingTime;

    @Schema(description = "휴식 시간 (hh:mm:ss)", example = "00:30:00")
    @NotNull(message = "Rest Time은 필수입니다.")
    private String restTime;

}