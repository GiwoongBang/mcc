package kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "서브 카테고리 응답")
@Getter
public class SubCategoryResponseDto {

    @Schema(description = "서브 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "메인 카테고리 정보 배열", example = "[1, '산']")
    private MainCategoryInfo mainCategoryInfo;

    @Schema(description = "서브 카테고리 이름", example = "북한산")
    private String name;

    @Schema(description = "위도", example = "37.6585")
    private double lat;

    @Schema(description = "경도", example = "126.9770")
    private double lng;

    @Schema(description = "고도(m)", example = "836.5")
    private double altitude;

    @Schema(description = "서브 카테고리 설명", example = "서울 근교에서 가장 인기 있는 산입니다.")
    private String description;

    @ArraySchema(arraySchema = @Schema(description = "안전 주의사항 목록\n예: [\"비 오는 날 미끄러움 주의\", \"겨울철 아이젠 착용 권장\"]"),
            schema = @Schema(example = "비 오는 날 미끄러움 주의"))
    private List<String> safetyNotes;

    @Schema(description = "장소 정보", example = "북한산국립공원은 서울과 경기도에 걸쳐 있는 산으로, 사계절 내내 인기 있는 명소입니다.")
    private String placeInfo;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/subcategory_thumbnail.jpg")
    private String thumbnailImg;

    @Schema(description = "수정일", example = "2024-03-02T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;

    @Builder
    private SubCategoryResponseDto(Long id, MainCategoryInfo mainCategoryInfo,
                                   String name, double lat, double lng, double altitude,
                                   String description, List<String> safetyNotes, String placeInfo,
                                   String thumbnailImg, LocalDateTime updatedAt, String message) {
        this.id = id;
        this.mainCategoryInfo = mainCategoryInfo;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
        this.description = description;
        this.safetyNotes = safetyNotes;
        this.placeInfo = placeInfo;
        this.thumbnailImg = thumbnailImg;
        this.updatedAt = updatedAt;
        this.message = message;
    }

    public static SubCategoryResponseDto of(SubCategoryEntity subCategoryEntity, String message) {
        SubCategoryResponseDto subCategory = SubCategoryResponseDto.builder()
                .id(subCategoryEntity.getId())
                .mainCategoryInfo(MainCategoryInfo.builder()
                        .id(subCategoryEntity.getMainCategory().getId())
                        .type(subCategoryEntity.getMainCategory().getType().getType())
                        .build())
                .name(subCategoryEntity.getName())
                .lat(subCategoryEntity.getLat())
                .lng(subCategoryEntity.getLng())
                .altitude(subCategoryEntity.getAltitude())
                .description(subCategoryEntity.getDescription())
                .safetyNotes(subCategoryEntity.getSafetyNotes())
                .placeInfo(subCategoryEntity.getPlaceInfo())
                .thumbnailImg(subCategoryEntity.getThumbnailImg())
                .updatedAt(subCategoryEntity.getUpdatedAt())
                .message(message)
                .build();

        return subCategory;
    }

    public static SubCategoryResponseDto minimalOf(SubCategoryEntity subCategoryEntity) {
        return SubCategoryResponseDto.builder()
                .id(subCategoryEntity.getId())
                .name(subCategoryEntity.getName())
                .build();
    }


    public static SubCategoryResponseDto withError(String message) {

        return SubCategoryResponseDto.builder()
                .message(message)
                .build();
    }

    @Getter
    @Builder
    public static class MainCategoryInfo {
        @Schema(description = "메인 카테고리 ID", example = "1")
        private Long id;

        @Schema(description = "메인 카테고리 타입", example = "산")
        private String type;
    }

}
