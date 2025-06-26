package kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.entities.DetailCategoryEntity;
import kr.co.mountaincc.maps.enums.DetailCategoryType;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "디테일 카테고리의 주요 정보만 포함된 간략한 응답")
@Getter
public class DetailCategoryMinimalResponseDto {

    @Schema(description = "디테일 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "디테일 카테고리 타입", example = "주차장")
    private DetailCategoryType type;

    @Schema(description = "상세 카테고리 제목", example = "A주차장")
    private String title;

    @Schema(description = "위도", example = "37.5665")
    private double lat;

    @Schema(description = "경도", example = "126.9780")
    private double lng;

    @Builder
    private DetailCategoryMinimalResponseDto(Long id, DetailCategoryType type, String title, double lat, double lng) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    public static DetailCategoryMinimalResponseDto of(DetailCategoryEntity detailCategoryEntity) {

        return DetailCategoryMinimalResponseDto.builder()
                .id(detailCategoryEntity.getId())
                .type(detailCategoryEntity.getType())
                .title(detailCategoryEntity.getTitle())
                .lat(detailCategoryEntity.getLat())
                .lng(detailCategoryEntity.getLng())
                .build();
    }

}
