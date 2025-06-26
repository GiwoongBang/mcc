package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomCourseListRequestDto {

    @Schema(description = "코스명", example = "가을 단풍 코스", required = true)
    @NotBlank(message = "Title은 필수입니다.")
    private String title;

    @Schema(description = "코스 설명", example = "아름다운 단풍을 즐길 수 있는 코스입니다.")
    private String description;

    @Schema(description = "거리(km)", example = "8.5")
    private Double distance;

    @Schema(description = "총 상승 고도(m)", example = "350.5")
    private Double elevationGain;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnailImg;

    @ArraySchema(
            arraySchema = @Schema(description = "CustomCourseRequestDto 리스트"),
            schema = @Schema(implementation = CustomCourseRequestDto.class)
    )
    @NotEmpty(message = "CustomCourseRequestDto 리스트는 비어 있을 수 없습니다.")
    @Valid
    private List<CustomCourseRequestDto> gpxMappings;

}
