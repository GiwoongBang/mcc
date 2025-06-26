package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.mainCategory.MainCategoryResponseDto;
import kr.co.mountaincc.maps.services.MainCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Main Category API", description = "Main Category API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/main-category")
@RestController
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    @Operation(summary = "메인 카테고리 데이터 조회", description = "메인 카테고리의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> getAllMainCategories() {
        try {

            return ResponseEntity.ok(mainCategoryService.getAllMainCategories());
        } catch (Exception e) {
            List<MainCategoryResponseDto> errorList = List.of(
                    MainCategoryResponseDto.withError("유효하지 않은 테이블: main-category || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(errorList);
        }
    }

    @Operation(summary = "메인 카테고리 데이터 삭제", description = "메인 카테고리에서 데이터를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMainCategory(@PathVariable("id") Long id) {

        String resData = mainCategoryService.deleteMainCategory(id);

        return ResponseEntity.ok(resData);
    }

}