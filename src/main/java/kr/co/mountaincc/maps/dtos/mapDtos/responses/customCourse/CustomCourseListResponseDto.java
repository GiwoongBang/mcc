package kr.co.mountaincc.maps.dtos.mapDtos.responses.customCourse;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "커스텀 코스 리스트 응답")
@Getter
public class CustomCourseListResponseDto {

    @Schema(description = "사용자 ID", example = "100")
    private Long userId;

    @Schema(description = "코스 ID", example = "200")
    private Long courseId;

    @Schema(description = "코스 제목", example = "나만의 산책 코스")
    private String title;

    @Schema(description = "코스 설명", example = "아름다운 단풍을 즐길 수 있는 코스입니다.")
    private String description;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnailImg;

    @Schema(description = "거리(km)", example = "8.5")
    private Double distance;

    @Schema(description = "총 상승 고도(m)", example = "350.5")
    private Double elevationGain;

    @ArraySchema(arraySchema = @Schema(description = "커스텀 코스 리스트"),
            schema = @Schema(implementation = CustomCourseResponseDto.class))
    private List<CustomCourseResponseDto> gpxMappings;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    public CustomCourseListResponseDto(Long userId, Long courseId, String title, String description, String thumbnailImg,
                                       double distance, double elevationGain,
                                       List<CustomCourseResponseDto> gpxMappings, String message) {
        this.userId = userId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.thumbnailImg = thumbnailImg;
        this.distance = distance;
        this.elevationGain = elevationGain;
        this.gpxMappings = gpxMappings;
        this.message = message;
    }

    public static CustomCourseListResponseDto of(Long userId, Long courseId, String title, String description, String thumbnailImg,
                                                 double distance, double elevationGain,
                                                 List<CustomCourseResponseDto> gpxMappings, String message) {

        return CustomCourseListResponseDto.builder()
                .userId(userId)
                .courseId(courseId)
                .title(title)
                .description(description)
                .thumbnailImg(thumbnailImg)
                .distance(distance)
                .elevationGain(elevationGain)
                .gpxMappings(gpxMappings)
                .message(message)
                .build();
    }

    public static CustomCourseListResponseDto withError(String message) {

        return CustomCourseListResponseDto.builder()
                .message(message)
                .build();
    }

}
