package kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "디테일 카테고리 단건 응답")
@Getter
public class DetailCategorySingleResponseDto {

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "디테일 카테고리 정보", implementation = DetailCategoryMinimalResponseDto.class)
    private DetailCategoryMinimalResponseDto detailCategory;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private DetailCategorySingleResponseDto(SubCategoryMinimalResponseDto subCategory,
                                            DetailCategoryMinimalResponseDto detailCategory,
                                            String message) {
        this.subCategory = subCategory;
        this.detailCategory = detailCategory;
        this.message = message;
    }

    public static DetailCategorySingleResponseDto of(SubCategoryMinimalResponseDto subCategory,
                                                     DetailCategoryMinimalResponseDto detailCategory,
                                                     String message) {
        return DetailCategorySingleResponseDto.builder()
                .subCategory(subCategory)
                .detailCategory(detailCategory)
                .message(message)
                .build();
    }

    public static DetailCategorySingleResponseDto withError(String message) {
        return DetailCategorySingleResponseDto.builder()
                .message(message)
                .build();
    }
}