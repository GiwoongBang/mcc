package kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "서브 카테고리 최소 정보(ID, Name) 응답")
@Getter
public class SubCategoryMinimalResponseDto {

    @Schema(description = "서브 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "서브 카테고리 이름", example = "북한산")
    private String name;

    @Builder
    private SubCategoryMinimalResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SubCategoryMinimalResponseDto of(SubCategoryEntity subCategoryEntity) {
        SubCategoryMinimalResponseDto subCategory = SubCategoryMinimalResponseDto.builder()
                .id(subCategoryEntity.getId())
                .name(subCategoryEntity.getName())
                .build();

        return subCategory;
    }

}