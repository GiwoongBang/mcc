package kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.entities.GpxEntity;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "GPX 최소 정보(ID, GPX URL) 응답")
@Getter
public class GpxMinimalResponseDto {

    @Schema(description = "GPX ID", example = "1")
    private Long id;

    @Schema(description = "GPX 파일 URL", example = "https://example.com/path/to/file.gpx")
    private String gpxUrl;

    @Builder
    private GpxMinimalResponseDto(Long id, String gpxUrl) {
        this.id = id;
        this.gpxUrl = gpxUrl;
    }

    public static GpxMinimalResponseDto of(GpxEntity gpxEntity) {

        return GpxMinimalResponseDto.builder()
                .id(gpxEntity.getId())
                .gpxUrl(gpxEntity.getGpxUrl())
                .build();
    }

}
