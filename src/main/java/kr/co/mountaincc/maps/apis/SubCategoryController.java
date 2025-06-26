package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.SubCategoryRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryResponseDto;
import kr.co.mountaincc.maps.services.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sub Category API", description = "Sub Category API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/sub-category")
@RestController
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Operation(summary = "서브 카테고리 데이터 조회",
            description = "서브 카테고리의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<List<SubCategoryResponseDto>> getAllSubCategories() {
        try {
            List<SubCategoryResponseDto> resData = subCategoryService.getAllSubCategories();

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<SubCategoryResponseDto> errorList = List.of(
                    SubCategoryResponseDto.withError("유효하지 않은 테이블: sub-category || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(errorList);
        }
    }

    @Operation(summary = "서브 카테고리 상세 데이터 조회",
            description = "특정 서브 카테고리 ID에 대한 상세 정보를 조회합니다.")
    @GetMapping("/{subCategoryId}")
    public ResponseEntity<SubCategoryResponseDto> getSubCategoryDetails(@PathVariable("subCategoryId") Long subCategoryId) {

        SubCategoryResponseDto resData = subCategoryService.getSubCategoryDetails(subCategoryId);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "서브 카테고리 데이터 생성",
            description = "새로운 서브 카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<SubCategoryResponseDto> createSubCategory(@RequestBody SubCategoryRequestDto requestDto) {

        SubCategoryResponseDto resData = subCategoryService.createSubCategory(requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "서브 카테고리 데이터 수정",
            description = "기존 서브 카테고리를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDto> updateSubCategory(@PathVariable("id") Long id,
                                        @RequestBody SubCategoryRequestDto requestDto) {

        SubCategoryResponseDto resData = subCategoryService.updateSubCategory(id, requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "서브 카테고리 데이터 삭제",
            description = "기존 서브 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable("id") Long id) {

        String resData = subCategoryService.deleteSubCategory(id);

        return ResponseEntity.ok(resData);
    }

}