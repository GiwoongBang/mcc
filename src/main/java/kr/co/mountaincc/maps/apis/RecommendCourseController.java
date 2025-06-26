package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.RecommendCourseListRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.recommendCourse.RecommendCourseListResponseDto;
import kr.co.mountaincc.maps.services.RecommendCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recommend Course API", description = "Recommend Course API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/mcc/recommend-course")
@RestController
public class RecommendCourseController {

    private final RecommendCourseService recommendCourseService;

    @Operation(summary = "Recommend Course 데이터 조회",
            description = "Recommend Course의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<Page<RecommendCourseListResponseDto>> getAllRecommendCourses(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            int effectivePage = (page <= 1) ? 1 : page;
            Pageable pageable = PageRequest.of(effectivePage - 1, size);

            Page<RecommendCourseListResponseDto> resData = recommendCourseService.getAllRecommendCourses(pageable);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<RecommendCourseListResponseDto> errorList = List.of(
                    RecommendCourseListResponseDto.withError("유효하지 않은 테이블: recommend-course || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(new PageImpl<>(errorList));
        }
    }

    @Operation(summary = "Recommend Course 상세 데이터 조회",
            description = "특정 Course ID에 해당하는 Recommend Course 상세 데이터를 조회합니다.")
    @GetMapping("/{courseId}")
    public ResponseEntity<RecommendCourseListResponseDto> getRecommendCourseByCourseId(@PathVariable("courseId") Long courseId) {

        RecommendCourseListResponseDto resData = recommendCourseService.getRecommendCourseByCourseId(courseId);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Recommend Course 데이터 생성",
            description = "새로운 Recommend Course 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<RecommendCourseListResponseDto> createRecommendCourse(@RequestBody RecommendCourseListRequestDto requestListDto) {

        RecommendCourseListResponseDto resData = recommendCourseService.createRecommendCourse(requestListDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Recommend Course 데이터 수정",
            description = "기존 Recommend Course 데이터를 수정합니다.")
    @PutMapping("/{courseId}")
    public ResponseEntity<RecommendCourseListResponseDto> updateRecommendCourse(@PathVariable("courseId") Long courseId,
                                                                                @RequestBody RecommendCourseListRequestDto requestListDto) {

        RecommendCourseListResponseDto resData = recommendCourseService.updateRecommendCourse(courseId, requestListDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Recommend Course 데이터 삭제",
            description = "기존 Recommend Course 데이터를 삭제합니다.")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteRecommendCourse(@PathVariable("courseId") Long courseId) {

        String resData = recommendCourseService.deleteRecommendCourse(courseId);

        return ResponseEntity.ok(resData);
    }

}