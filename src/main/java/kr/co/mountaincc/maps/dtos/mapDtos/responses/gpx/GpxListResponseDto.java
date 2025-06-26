package kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.GpxEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "GPX 정보 응답")
@Getter
public class GpxListResponseDto {

    @Schema(description = "GPX ID", example = "1")
    private Long id;

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @Schema(description = "출발 지점 정보", implementation = DetailCategoryMinimalResponseDto.class)
    private DetailCategoryMinimalResponseDto startPoint;

    @Schema(description = "도착 지점 정보", implementation = DetailCategoryMinimalResponseDto.class)
    private DetailCategoryMinimalResponseDto endPoint;

    @Schema(description = "GPX 파일 URL", example = "https://example.com/path/to/file.gpx")
    private String gpxUrl;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;


    @Builder
    private GpxListResponseDto(Long id, SubCategoryMinimalResponseDto subCategory,
                               DetailCategoryMinimalResponseDto startPoint, DetailCategoryMinimalResponseDto endPoint,
                               String gpxUrl, LocalDateTime updatedAt, String message) {
        this.id = id;
        this.subCategory = subCategory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.gpxUrl = gpxUrl;
        this.updatedAt = updatedAt;
        this.message = message;
    }

    public static GpxListResponseDto of(GpxEntity gpxEntity, String message) {

        return GpxListResponseDto.builder()
                .id(gpxEntity.getId())
                .subCategory(SubCategoryMinimalResponseDto.of(gpxEntity.getSubCategory()))
                .startPoint(DetailCategoryMinimalResponseDto.of(gpxEntity.getStartPoint()))
                .endPoint(DetailCategoryMinimalResponseDto.of(gpxEntity.getEndPoint()))
                .gpxUrl(gpxEntity.getGpxUrl())
                .updatedAt(gpxEntity.getUpdatedAt())
                .message(message)
                .build();
    }

    public static GpxListResponseDto withError(String message) {

        return GpxListResponseDto.builder()
                .message(message)
                .build();
    }

}
