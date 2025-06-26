package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.TagRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.TagResponseDto;
import kr.co.mountaincc.maps.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag API", description = "Tag API 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mcc/tag")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Tag 데이터 조회",
            description = "Tag의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        try {
            List<TagResponseDto> resData = tagService.getAllTags();

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<TagResponseDto> errorList = List.of(
                    TagResponseDto.withError("유효하지 않은 테이블: tag || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(errorList);
        }
    }

    @Operation(summary = "Tag 데이터 생성",
            description = "새로운 Tag 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto requestDto) {

        TagResponseDto resData = tagService.createTag(requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Tag 데이터 수정",
            description = "기존 Tag 데이터를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable("id") Long id,
                                                     @RequestBody TagRequestDto requestDto) {

        TagResponseDto resData = tagService.updateTag(id, requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Tag 데이터 삭제",
            description = "기존 Tag 데이터를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable("id") Long id) {

        String resData = tagService.deleteTag(id);

        return ResponseEntity.ok(resData);
    }
}