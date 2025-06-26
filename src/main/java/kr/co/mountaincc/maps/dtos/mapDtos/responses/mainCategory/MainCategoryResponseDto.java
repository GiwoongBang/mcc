package kr.co.mountaincc.maps.dtos.mapDtos.responses.mainCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.entities.MainCategoryEntity;
import kr.co.mountaincc.maps.enums.MainCategoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "메인 카테고리 응답")
@Getter
public class MainCategoryResponseDto {

    @Schema(description = "메인 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "메인 카테고리 타입", example = "산")
    private MainCategoryType type;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private MainCategoryResponseDto(Long id, MainCategoryType type, LocalDateTime updatedAt, String message) {
        this.id = id;
        this.type = type;
        this.updatedAt = updatedAt;
        this.message = message;
    }

    public static MainCategoryResponseDto of(MainCategoryEntity mainCategoryEntity, String message) {

        return MainCategoryResponseDto.builder()
                .id(mainCategoryEntity.getId())
                .type(mainCategoryEntity.getType())
                .updatedAt(mainCategoryEntity.getUpdatedAt())
                .message(message)
                .build();
    }

    public static MainCategoryResponseDto withError(String message) {

        return MainCategoryResponseDto.builder()
                .message(message)
                .build();
    }

}