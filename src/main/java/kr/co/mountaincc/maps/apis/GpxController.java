package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.GpxRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx.GpxDetailResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx.GpxListResponseDto;
import kr.co.mountaincc.maps.services.GpxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "GPX API", description = "GPX API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/gpx")
@RestController
public class GpxController {

    private final GpxService gpxService;

    @Operation(summary = "Gpx 데이터 조회",
            description = "Gpx의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<Page<GpxListResponseDto>> getAllGpxs(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            int effectivePage = (page <= 1) ? 1 : page;
            Pageable pageable = PageRequest.of(effectivePage - 1, size);

            Page<GpxListResponseDto> resData = gpxService.getAllGpxs(pageable);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<GpxListResponseDto> errorList = List.of(
                    GpxListResponseDto.withError("유효하지 않은 테이블: gpx || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(new PageImpl<>(errorList));
        }
    }

    @Operation(summary = "Gpx 상세 데이터 조회",
            description = "특정 SubCategory ID에 해당하는 Gpx 상세 데이터를 조회합니다.")
    @GetMapping("/{subCategoryId}")
    public ResponseEntity<GpxDetailResponseDto> getGpxDetails(@PathVariable("subCategoryId") Long subCategoryId) {

        GpxDetailResponseDto resData = gpxService.getGpxDetails(subCategoryId);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Gpx 데이터 생성",
            description = "새로운 Gpx 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<GpxListResponseDto> createGpx(@RequestBody GpxRequestDto requestDto) {

        GpxListResponseDto resData = gpxService.createGpx(requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Gpx 데이터 수정",
            description = "기존 Gpx 데이터를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<GpxListResponseDto> updateGpx(@PathVariable("id") Long id,
                                                        @RequestBody GpxRequestDto requestDto) {

        GpxListResponseDto resData = gpxService.updateGpx(id, requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Gpx 데이터 삭제",
            description = "기존 Gpx 데이터를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGpx(@PathVariable("id") Long id) {

        String resData = gpxService.deleteGpx(id);

        return ResponseEntity.ok(resData);
    }

}