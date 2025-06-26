package kr.co.mountaincc.maps.dtos.mapDtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.TagEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "태그 응답")
@Getter
public class TagResponseDto {

    @Schema(description = "태그 ID", example = "1")
    private Long id;

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "태그 내용", example = "가을추천")
    private String tag;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private TagResponseDto(Long id, SubCategoryMinimalResponseDto subCategory,
                           String tag, LocalDateTime updatedAt, String message) {
        this.id = id;
        this.subCategory = subCategory;
        this.tag = tag;
        this.updatedAt = updatedAt;
        this.message = message;
    }

    public static TagResponseDto of(TagEntity tagEntity, String message) {

        return TagResponseDto.builder()
                .id(tagEntity.getId())
                .subCategory(SubCategoryMinimalResponseDto.of(tagEntity.getSubCategory()))
                .tag(tagEntity.getTag())
                .updatedAt(tagEntity.getUpdatedAt())
                .message(message)
                .build();
    }

    public static TagResponseDto withError(String message) {

        return TagResponseDto.builder()
                .message(message)
                .build();
    }

}
