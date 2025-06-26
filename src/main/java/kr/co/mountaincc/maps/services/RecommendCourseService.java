package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.RecommendCourseListRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.RecommendCourseRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.recommendCourse.RecommendCourseListResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.recommendCourse.RecommendCourseResponseDto;
import kr.co.mountaincc.maps.entities.GpxEntity;
import kr.co.mountaincc.maps.entities.RecommendCourseCommonEntity;
import kr.co.mountaincc.maps.entities.RecommendCourseDetailEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.repositories.GpxRepository;
import kr.co.mountaincc.maps.repositories.RecommendCourseCommonRepository;
import kr.co.mountaincc.maps.repositories.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class RecommendCourseService {

    private final RecommendCourseCommonRepository recommendCourseCommonRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final GpxRepository gpxRepository;

    private static final String SUCCESS_MESSAGE = "성공적으로 처리됐습니다.";

    public Page<RecommendCourseListResponseDto> getAllRecommendCourses(Pageable pageable) {
        Page<RecommendCourseCommonEntity> allList = recommendCourseCommonRepository.findAll(pageable);

        List<RecommendCourseListResponseDto> resData = allList.getContent().stream()
                .map(common -> {
                    List<RecommendCourseResponseDto> details = common.getDetails().stream()
                            .map(RecommendCourseResponseDto::of)
                            .collect(Collectors.toList());

                    return RecommendCourseListResponseDto.of(
                            common.getId(),
                            common.getTitle(),
                            common.getDescription(),
                            common.getThumbnailImg(),
                            common.getDistance(),
                            common.getElevationGain(),
                            details,
                            SUCCESS_MESSAGE
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(resData, pageable, allList.getTotalElements());
    }

    public RecommendCourseListResponseDto getRecommendCourseByCourseId(Long courseId) {

        try {
            Optional<RecommendCourseCommonEntity> recommendCourseCommon = recommendCourseCommonRepository.findById(courseId);
            if (recommendCourseCommon.isEmpty()) {
                throw new IllegalArgumentException("No courses found for Course ID: " + courseId);
            }

            RecommendCourseCommonEntity common = recommendCourseCommon.get();
            List<RecommendCourseResponseDto> details = common.getDetails().stream()
                    .map(RecommendCourseResponseDto::of)
                    .toList();

            return RecommendCourseListResponseDto.of(
                    common.getId(),
                    common.getTitle(),
                    common.getDescription(),
                    common.getThumbnailImg(),
                    common.getDistance(),
                    common.getElevationGain(),
                    details,
                    SUCCESS_MESSAGE
            );
        } catch (IllegalArgumentException e) {

            return RecommendCourseListResponseDto.withError("RecommendCourse retrieval failed: " + e.getMessage());
        } catch (Exception e) {

            return RecommendCourseListResponseDto.withError("Server error occurred while retrieving RecommendCourse: " + e.getMessage());
        }
    }

    @Transactional
    public RecommendCourseListResponseDto createRecommendCourse(RecommendCourseListRequestDto recommendCourseListRequestDto) {
        try {
            String title = recommendCourseListRequestDto.getTitle();
            String description = recommendCourseListRequestDto.getDescription();
            Double distance = recommendCourseListRequestDto.getDistance();
            Double elevationGain = recommendCourseListRequestDto.getElevationGain();
            String thumbnailImg = recommendCourseListRequestDto.getThumbnailImg();

            RecommendCourseCommonEntity common = RecommendCourseCommonEntity.of(
                    title,
                    description,
                    distance != null ? distance : 0.0,
                    elevationGain != null ? elevationGain : 0.0,
                    thumbnailImg
            );

            List<RecommendCourseDetailEntity> details = IntStream.range(0, recommendCourseListRequestDto.getGpxMappings().size())
                    .mapToObj(index -> {
                        RecommendCourseRequestDto mappingDto = recommendCourseListRequestDto.getGpxMappings().get(index);
                        SubCategoryEntity subCategory = subCategoryRepository.findById(mappingDto.getSubCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + mappingDto.getSubCategoryId()));
                        GpxEntity gpx = gpxRepository.findById(mappingDto.getGpxId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid GPX ID: " + mappingDto.getGpxId()));
                        int order = index + 1;
                        return RecommendCourseDetailEntity.of(common, subCategory, order, gpx);
                    })
                    .collect(Collectors.toList());

            common.getDetails().addAll(details);

            recommendCourseCommonRepository.save(common);

            List<RecommendCourseResponseDto> responseDetails = common.getDetails().stream()
                    .map(RecommendCourseResponseDto::of)
                    .collect(Collectors.toList());

            return RecommendCourseListResponseDto.of(
                    common.getId(),
                    common.getTitle(),
                    common.getDescription(),
                    common.getThumbnailImg(),
                    common.getDistance(),
                    common.getElevationGain(),
                    responseDetails,
                    SUCCESS_MESSAGE
            );
        } catch (IllegalArgumentException e) {

            return RecommendCourseListResponseDto.withError("RecommendCourse addition failed: " + e.getMessage());
        } catch (Exception e) {

            return RecommendCourseListResponseDto.withError("Server error occurred while adding RecommendCourse: " + e.getMessage());
        }
    }

    @Transactional
    public RecommendCourseListResponseDto updateRecommendCourse(Long courseId,
                                                                RecommendCourseListRequestDto recommendCourseListRequestDto) {
        try {
            Optional<RecommendCourseCommonEntity> recommendCourseCommon = recommendCourseCommonRepository.findById(courseId);
            if (recommendCourseCommon.isEmpty()) {
                throw new IllegalArgumentException("No RecommendCourse exists for Course ID: " + courseId);
            }
            recommendCourseCommonRepository.delete(recommendCourseCommon.get());

            return createRecommendCourse(recommendCourseListRequestDto);
        } catch (IllegalArgumentException e) {

            return RecommendCourseListResponseDto.withError("RecommendCourse update failed: " + e.getMessage());
        } catch (Exception e) {

            return RecommendCourseListResponseDto.withError("Server error occurred while updating RecommendCourse: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteRecommendCourse(Long courseId) {
        try {
            Optional<RecommendCourseCommonEntity> recommendCourseCommon = recommendCourseCommonRepository.findById(courseId);
            if (recommendCourseCommon.isEmpty()) {

                throw new IllegalArgumentException("No RecommendCourse exists for Course ID: " + courseId);
            }

            recommendCourseCommonRepository.delete(recommendCourseCommon.get());

            return "RecommendCourse deletion successful. Course ID: " + courseId;
        } catch (IllegalArgumentException e) {

            return "RecommendCourse deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deleting RecommendCourse: " + e.getMessage();
        }

    }

}