package kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "요청한 서브카테고리에 해당하는 디테일 카테고리 목록 응답")
@Getter
public class DetailCategoryDetailResponseDto {

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "디테일 카테고리 타입", example = "주차장")
    private List<DetailCategoryMinimalResponseDto> detailCategories;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private DetailCategoryDetailResponseDto(SubCategoryMinimalResponseDto subCategory,
                                            List<DetailCategoryMinimalResponseDto> detailCategories, String message) {
        this.subCategory = subCategory;
        this.detailCategories = detailCategories;
        this.message = message;
    }

    public static DetailCategoryDetailResponseDto of(SubCategoryMinimalResponseDto subCategory,
                                                     List<DetailCategoryMinimalResponseDto> detailCategories, String message) {

        return DetailCategoryDetailResponseDto.builder()
                .subCategory(subCategory)
                .detailCategories(detailCategories)
                .message(message)
                .build();
    }

    public static DetailCategoryDetailResponseDto withError(String message) {

        return DetailCategoryDetailResponseDto.builder()
                .message(message)
                .build();
    }

}
