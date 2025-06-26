package kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.DetailCategoryEntity;
import kr.co.mountaincc.maps.enums.DetailCategoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "디테일 카테고리 응답")
@Getter
public class DetailCategoryListResponseDto {

    @Schema(description = "디테일 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "디테일 카테고리 타입", example = "주차장")
    private DetailCategoryType type;

    @Schema(description = "상세 카테고리 제목", example = "A주차장")
    private String title;

    @Schema(description = "위도", example = "37.5665")
    private double lat;

    @Schema(description = "경도", example = "126.9780")
    private double lng;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private DetailCategoryListResponseDto(Long id, SubCategoryMinimalResponseDto subCategory, DetailCategoryType type,
                                          String title, double lat, double lng, LocalDateTime updatedAt, String message) {
        this.id = id;
        this.subCategory = subCategory;
        this.type = type;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.updatedAt = updatedAt;
        this.message = message;
    }

    public static DetailCategoryListResponseDto of(DetailCategoryEntity detailCategoryEntity, String message) {

        return DetailCategoryListResponseDto.builder()
                .id(detailCategoryEntity.getId())
                .subCategory(SubCategoryMinimalResponseDto.of(detailCategoryEntity.getSubCategory()))
                .type(detailCategoryEntity.getType())
                .title(detailCategoryEntity.getTitle())
                .lat(detailCategoryEntity.getLat())
                .lng(detailCategoryEntity.getLng())
                .updatedAt(detailCategoryEntity.getUpdatedAt())
                .message(message)
                .build();
    }

    public static DetailCategoryListResponseDto info(DetailCategoryEntity detailCategoryEntity, String message) {

        return DetailCategoryListResponseDto.builder()
                .id(detailCategoryEntity.getId())
                .type(detailCategoryEntity.getType())
                .title(detailCategoryEntity.getTitle())
                .lat(detailCategoryEntity.getLat())
                .lng(detailCategoryEntity.getLng())
                .updatedAt(detailCategoryEntity.getUpdatedAt())
                .message(message)
                .build();
    }

    public static DetailCategoryListResponseDto minimalOf(DetailCategoryEntity detailCategoryEntity) {
        return DetailCategoryListResponseDto.builder()
                .id(detailCategoryEntity.getId())
                .title(detailCategoryEntity.getTitle())
                .build();
    }

    public static DetailCategoryListResponseDto groupOf(SubCategoryMinimalResponseDto subCategory, String message) {
        return DetailCategoryListResponseDto.builder()
                .subCategory(subCategory)
                .message(message)
                .build();
    }

    public static DetailCategoryListResponseDto withError(String message) {

        return DetailCategoryListResponseDto.builder()
                .message(message)
                .build();
    }

}
