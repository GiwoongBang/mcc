package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TagRequestDto {

    @Schema(description = "태그가 속한 SubCategory ID", example = "1")
    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @Schema(description = "태그 내용", example = "가을추천")
    @NotBlank(message = "Tag는 필수입니다.")
    private String tag;

}