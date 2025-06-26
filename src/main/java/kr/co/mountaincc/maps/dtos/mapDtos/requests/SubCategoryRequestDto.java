package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SubCategoryRequestDto {

    @Schema(description = "상위 MainCategory ID", example = "1")
    @NotNull(message = "MainCategory ID는 필수입니다.")
    private Long mainCategoryId;

    @Schema(description = "SubCategory 이름", example = "북한산")
    @NotBlank(message = "SubCategory Name은 필수입니다.")
    private String name;

    @Schema(description = "위도", example = "37.6585")
    @NotNull(message = "Latitude는 필수입니다.")
    private Double lat;

    @Schema(description = "경도", example = "126.9770")
    @NotNull(message = "Longitude는 필수입니다.")
    private Double lng;

    @Schema(description = "고도(m)", example = "836.5")
    private Double altitude;

    @Schema(description = "설명", example = "서울 근교에서 가장 인기 있는 산입니다.")
    private String description;

    @Schema(description = "안전 주의사항 목록", example = "[\"비 오는 날 미끄러움 주의\", \"겨울철 아이젠 착용\"]")
    private List<String> safetyNotes;

    @Schema(description = "장소 정보",
            example = "북한산국립공원은 서울과 경기도에 걸쳐 있는 산으로, 아름다운 계곡과 암릉이 조화를 이루며 사계절 내내 등산객에게 사랑받는 명소입니다.")
    private String placeInfo;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/subcategory_thumbnail.jpg")
    private String thumbnailImg;

}