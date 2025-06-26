package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.DetailCategoryRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryDetailResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryListResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategorySingleResponseDto;
import kr.co.mountaincc.maps.services.DetailCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Detail Category API", description = "Detail Category API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/detail-category")
@RestController
public class DetailCategoryController {

    private final DetailCategoryService detailCategoryService;

    @Operation(summary = "상세 카테고리 데이터 조회",
            description = "상세 카테고리의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<Page<DetailCategoryListResponseDto>> getAllDetailCategories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            int effectivePage = (page <= 1) ? 1 : page;
            Pageable pageable = PageRequest.of(effectivePage - 1, size);

            Page<DetailCategoryListResponseDto> resData = detailCategoryService.getAllDetailCategories(pageable);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<DetailCategoryListResponseDto> errorList = List.of(
                    DetailCategoryListResponseDto.withError("유효하지 않은 테이블: detail-category || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(new PageImpl<>(errorList));
        }
    }

    @Operation(summary = "상세 카테고리 상세 데이터 조회",
            description = "특정 SubCategory ID에 해당하는 상세 카테고리 정보를 조회합니다.")
    @GetMapping("/{subCategoryId}")
    public ResponseEntity<DetailCategoryDetailResponseDto> getDetailCategoryDetails(@PathVariable("subCategoryId") Long subCategoryId) {

        DetailCategoryDetailResponseDto resData = detailCategoryService.getDetailCategoryDetails(subCategoryId);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "상세 카테고리 데이터 생성", description = "새로운 상세 카테고리 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<DetailCategorySingleResponseDto> createDetailCategory(@RequestBody DetailCategoryRequestDto requestDto) {

        DetailCategorySingleResponseDto resData = detailCategoryService.createDetailCategory(requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "상세 카테고리 데이터 수정",
            description = "기존 상세 카테고리 데이터를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<DetailCategorySingleResponseDto> updateDetailCategory(@PathVariable("id") Long id,
                                                                              @RequestBody DetailCategoryRequestDto requestDto) {

        DetailCategorySingleResponseDto resData = detailCategoryService.updateDetailCategory(id, requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "상세 카테고리 데이터 삭제",
            description = "기존 상세 카테고리 데이터를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDetailCategory(@PathVariable("id") Long id) {

        String resData = detailCategoryService.deleteDetailCategory(id);

        return ResponseEntity.ok(resData);
    }

}