package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GpxRequestDto {

    @Schema(description = "GPX가 속한 SubCategory ID", example = "1")
    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @Schema(description = "출발 지점의 DetailCategory ID", example = "10")
    @NotNull(message = "startPoint ID는 필수입니다.")
    private Long startPointId;

    @Schema(description = "도착 지점의 DetailCategory ID", example = "20")
    @NotNull(message = "endPoint ID는 필수입니다.")
    private Long endPointId;

    @Schema(description = "GPX 파일이 저장된 경로(URL)", example = "https://example.com/path/to/file.gpx")
    @NotNull(message = "GPX Url은 필수입니다.")
    private String gpxUrl;

}