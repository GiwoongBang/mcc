package kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "GPX 정보 응답")
@Getter
public class GpxDetailResponseDto {

    @Schema(description = "서브 카테고리 정보", implementation = SubCategoryMinimalResponseDto.class)
    private SubCategoryMinimalResponseDto subCategory;

    @ArraySchema(
            schema = @Schema(implementation = GpxListResponseDto.class),
            arraySchema = @Schema(description = "GPX 목록"))
    private List<GpxListResponseDto> gpxMappings;

    @Schema(description = "처리 결과 메시지", example = "성공적으로 처리됐습니다.")
    private String message;


    @Builder
    private GpxDetailResponseDto(SubCategoryMinimalResponseDto subCategory, List<GpxListResponseDto> gpxMappings, String message) {
        this.subCategory = subCategory;
        this.gpxMappings = gpxMappings;
        this.message = message;
    }

    public static GpxDetailResponseDto of(SubCategoryMinimalResponseDto subCategory,
                                          List<GpxListResponseDto> gpxMappings, String message) {
        return GpxDetailResponseDto.builder()
                .subCategory(subCategory)
                .gpxMappings(gpxMappings)
                .message(message)
                .build();
    }

    public static GpxDetailResponseDto withError(String message) {

        return GpxDetailResponseDto.builder()
                .message(message)
                .build();
    }

}
