package kr.co.mountaincc.maps.dtos.mapDtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MainCategoryRequestDto {

    @Schema(description = "MainCategory 타입", example = "산")
    @NotNull(message = "MainCategory Type은 필수입니다.")
    private String type;

}