package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.CustomCourseListRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.customCourse.CustomCourseListResponseDto;
import kr.co.mountaincc.maps.services.CustomCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Custom Course API", description = "Custom Course API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/custom-course")
@RestController
public class CustomCourseController {

    private final CustomCourseService customCourseService;

    @Operation(summary = "Custom Course 데이터 조회",
            description = "Custom Course의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<Page<CustomCourseListResponseDto>> getAllCustomCourses(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            int effectivePage = (page <= 1) ? 1 : page;
            Pageable pageable = PageRequest.of(effectivePage - 1, size);

            Page<CustomCourseListResponseDto> resData = customCourseService.getAllCustomCourses(pageable);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {

            List<CustomCourseListResponseDto> errorList = List.of(
                    CustomCourseListResponseDto.withError("유효하지 않은 테이블: custom-course || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(new PageImpl<>(errorList));
        }
    }

    @Operation(summary = "Custom Course 상세 데이터 조회",
            description = "특정 Course ID에 해당하는 Custom Course 상세 데이터를 조회합니다.")
    @GetMapping("/{courseId}")
    public ResponseEntity<CustomCourseListResponseDto> getCustomCourseByCourseId(@PathVariable("courseId") Long courseId) {

        CustomCourseListResponseDto resData = customCourseService.getCustomCourseByCourseId(courseId);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Custom Course 데이터 생성",
            description = "새로운 Custom Course 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<CustomCourseListResponseDto> createCustomCourse(@RequestBody CustomCourseListRequestDto requestListDto) {

        CustomCourseListResponseDto resData = customCourseService.createCustomCourse(requestListDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Custom Course 데이터 수정",
            description = "기존 Custom Course 데이터를 수정합니다.")
    @PutMapping("/{courseId}")
    public ResponseEntity<CustomCourseListResponseDto> updateCustomCourse(@PathVariable("courseId") Long courseId,
                                                                          @RequestBody CustomCourseListRequestDto requestListDto) {

        CustomCourseListResponseDto resData = customCourseService.updateCustomCourse(courseId, requestListDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Custom Course 데이터 삭제",
            description = "기존 Custom Course 데이터를 삭제합니다.")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCustomCourse(@PathVariable("courseId") Long courseId) {

        String resData = customCourseService.deleteCustomCourse(courseId);

        return ResponseEntity.ok(resData);
    }

}