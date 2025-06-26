package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RecommendCourseRequestDto {

    @Schema(description = "추천 코스가 속한 SubCategory ID", example = "1", required = true)
    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @Schema(description = "특정 구간의 GPX ID", example = "2", required = true)
    @NotNull(message = "GPX ID는 필수입니다.")
    private Long gpxId;

}