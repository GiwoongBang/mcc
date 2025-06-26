package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DetailCategoryRequestDto {

    @Schema(description = "상위 SubCategory ID", example = "1")
    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @Schema(description = "상세 카테고리 타입\n" +
            "가능한 값: 주차장, 대피소, 갈림길, 절, 봉우리, 헬기장, 시작점, 들머리, 명소, 정상, 화장실",
            example = "주차장")
    @NotNull(message = "DetailCategory Type은 필수입니다.")
    private String type;

    @Schema(description = "상세 카테고리 제목", example = "A주차장")
    @NotBlank(message = "Title은 필수입니다.")
    private String title;

    @Schema(description = "위도", example = "37.5665")
    @NotNull(message = "Latitude는 필수입니다.")
    private Double lat;

    @Schema(description = "경도", example = "126.9780")
    @NotNull(message = "Longitude는 필수입니다.")
    private Double lng;

}