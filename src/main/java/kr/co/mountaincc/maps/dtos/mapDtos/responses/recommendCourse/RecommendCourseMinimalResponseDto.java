package kr.co.mountaincc.maps.dtos.mapDtos.responses.recommendCourse;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx.GpxMinimalResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryResponseDto;
import kr.co.mountaincc.maps.entities.RecommendCourseDetailEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "추천 코스 응답")
@Getter
public class RecommendCourseMinimalResponseDto {

    @Schema(description = "추천 코스 ID", example = "1")
    private Long id;

    @Schema(description = "추천 코스 순서", example = "1")
    private int order;

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "GPX 정보", implementation = GpxMinimalResponseDto.class)
    private GpxMinimalResponseDto gpx;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Builder
    private RecommendCourseMinimalResponseDto(Long id, int order, SubCategoryMinimalResponseDto subCategory,
                                              GpxMinimalResponseDto gpx, LocalDateTime updatedAt) {
        this.id = id;
        this.order = order;
        this.subCategory = subCategory;
        this.gpx = gpx;
        this.updatedAt = updatedAt;
    }

    public static RecommendCourseMinimalResponseDto of(RecommendCourseDetailEntity detail) {

        return RecommendCourseMinimalResponseDto.builder()
                .id(detail.getId())
                .order(detail.getOrder())
                .subCategory(SubCategoryMinimalResponseDto.of(detail.getSubCategory()))
                .gpx(GpxMinimalResponseDto.of(detail.getGpx()))
                .updatedAt(detail.getRecommendCourseCommon().getUpdatedAt())
                .build();
    }

}
