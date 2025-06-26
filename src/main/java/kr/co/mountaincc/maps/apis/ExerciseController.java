package kr.co.mountaincc.maps.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.ExerciseRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.ExerciseResponseDto;
import kr.co.mountaincc.maps.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Exercise API", description = "Exercise API 입니다.")
@RestController
@RequestMapping("/mcc/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(summary = "Exercise 데이터 조회",
            description = "Exercise의 전체 데이터를 조회합니다. (페이지네이션 미적용)")
    @GetMapping
    public ResponseEntity<Page<ExerciseResponseDto>> getAllExercises(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            int effectivePage = (page <= 1) ? 1 : page;
            Pageable pageable = PageRequest.of(effectivePage - 1, size);

            Page<ExerciseResponseDto> resData = exerciseService.getAllExercises(pageable);

            return ResponseEntity.ok(resData);
        } catch (Exception e) {
            List<ExerciseResponseDto> errorList = List.of(
                    ExerciseResponseDto.withError("유효하지 않은 테이블: exercise || " + e.getMessage())
            );

            return ResponseEntity.badRequest().body(new PageImpl<>(errorList));
        }
    }

    @Operation(summary = "Exercise 상세 데이터 조회",
            description = "특정 Exercise ID에 해당하는 데이터를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> getExerciseDetails(@PathVariable("id") Long id) {

        ExerciseResponseDto resData = exerciseService.getExerciseDetails(id);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Exercise 데이터 생성", description = "새로운 Exercise 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<ExerciseResponseDto> createExercise(@RequestBody ExerciseRequestDto requestDto) {

        ExerciseResponseDto resData = exerciseService.createExercise(requestDto);

        return ResponseEntity.ok(resData);
    }

    @Operation(summary = "Exercise 데이터 삭제", description = "기존 Exercise 데이터를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable("id") Long id) {

        String resData = exerciseService.deleteExercise(id);

        return ResponseEntity.ok(resData);
    }

}