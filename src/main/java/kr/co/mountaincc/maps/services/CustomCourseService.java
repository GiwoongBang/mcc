package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.CustomCourseListRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.CustomCourseRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.customCourse.CustomCourseListResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.customCourse.CustomCourseResponseDto;
import kr.co.mountaincc.maps.entities.*;
import kr.co.mountaincc.maps.repositories.CustomCourseRepository;
import kr.co.mountaincc.maps.repositories.GpxRepository;
import kr.co.mountaincc.maps.repositories.UserRepository;
import kr.co.mountaincc.maps.repositories.SubCategoryRepository;
import kr.co.mountaincc.users.oauth2.CustomOAuth2UserEntity;
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
public class CustomCourseService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CustomCourseRepository customCourseRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final GpxRepository gpxRepository;

    private static final String SUCCESS_MESSAGE = null;

    public Page<CustomCourseListResponseDto> getAllCustomCourses(Pageable pageable) {
        Page<CustomCourseCommonEntity> allList = customCourseRepository.findAll(pageable);

        List<CustomCourseListResponseDto> resData = allList.getContent().stream()
                .map(common -> {
                    Long userId = common.getUser().getId();
                    List<CustomCourseResponseDto> details = common.getDetails().stream()
                            .map(detail -> CustomCourseResponseDto.of(detail, detail.getGpx()))
                            .collect(Collectors.toList());

                    return CustomCourseListResponseDto.of(
                            userId,
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

    public CustomCourseListResponseDto getCustomCourseByCourseId(Long courseId) {
        try {
            Optional<CustomCourseCommonEntity> customCourseCommon = customCourseRepository.findById(courseId);
            if (customCourseCommon.isEmpty()) {
                throw new IllegalArgumentException("No courses found for Course ID: " + courseId);
            }

            CustomCourseCommonEntity common = customCourseCommon.get();
            Long userId = common.getUser().getId();
            List<CustomCourseResponseDto> details = common.getDetails().stream()
                    .map(detail -> CustomCourseResponseDto.of(detail, detail.getGpx()))
                    .toList();

            return CustomCourseListResponseDto.of(
                    userId,
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

            return CustomCourseListResponseDto.withError("CustomCourse retrieval failed: " + e.getMessage());
        } catch (Exception e) {

            return CustomCourseListResponseDto.withError("Server error occurred while retrieving CustomCourse: " + e.getMessage());
        }
    }

    @Transactional
    public CustomCourseListResponseDto createCustomCourse(CustomCourseListRequestDto customCourseListRequestDto) {
        CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();

        try {
            UserEntity foundUser = userRepository.findByUsername(customUser.getUsername());
            if (foundUser == null) {
                throw new IllegalArgumentException("User does not exist: " + customUser.getUsername());
            }
            Long userId = foundUser.getId();

            String title = customCourseListRequestDto.getTitle();
            String description = customCourseListRequestDto.getDescription();
            Double distance = customCourseListRequestDto.getDistance();
            Double elevationGain = customCourseListRequestDto.getElevationGain();
            String thumbnailImg = customCourseListRequestDto.getThumbnailImg();

            CustomCourseCommonEntity common = CustomCourseCommonEntity.of(
                    foundUser,
                    title,
                    description,
                    distance != null ? distance : 0.0,
                    elevationGain != null ? elevationGain : 0.0,
                    thumbnailImg
            );

            List<CustomCourseDetailEntity> details = IntStream.range(0, customCourseListRequestDto.getGpxMappings().size())
                    .mapToObj(index -> {
                        CustomCourseRequestDto mappingDto = customCourseListRequestDto.getGpxMappings().get(index);
                        SubCategoryEntity subCategory = subCategoryRepository.findById(mappingDto.getSubCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + mappingDto.getSubCategoryId()));
                        GpxEntity gpx = gpxRepository.findById(mappingDto.getGpxId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid GPX ID: " + mappingDto.getGpxId()));
                        int order = index + 1;
                        return CustomCourseDetailEntity.of(common, subCategory, order, gpx);
                    })
                    .toList();

            common.getDetails().addAll(details);

            customCourseRepository.save(common);

            List<CustomCourseResponseDto> createdResults = common.getDetails().stream()
                    .map(detail -> CustomCourseResponseDto.of(detail, detail.getGpx()))
                    .collect(Collectors.toList());

            return CustomCourseListResponseDto.of(
                    userId,
                    common.getId(),
                    common.getTitle(),
                    common.getDescription(),
                    common.getThumbnailImg(),
                    common.getDistance(),
                    common.getElevationGain(),
                    createdResults,
                    SUCCESS_MESSAGE
            );
        } catch (IllegalArgumentException e) {

            return CustomCourseListResponseDto.withError("CustomCourse addition failed: " + e.getMessage());
        } catch (Exception e) {

            return CustomCourseListResponseDto.withError("Server error occurred while adding CustomCourse: " + e.getMessage());
        }
    }

    @Transactional
    public CustomCourseListResponseDto updateCustomCourse(Long courseId, CustomCourseListRequestDto customCourseListRequestDto) {
        CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();

        try {
            UserEntity foundUser = userRepository.findByUsername(customUser.getUsername());
            if (foundUser == null) {
                throw new IllegalArgumentException("User does not exist: " + customUser.getUsername());
            }

            Optional<CustomCourseCommonEntity> optionalCommon = customCourseRepository.findById(courseId);
            if (optionalCommon.isEmpty()) {
                throw new IllegalArgumentException("No CustomCourse exists for Course ID: " + courseId);
            }

            customCourseRepository.delete(optionalCommon.get());

            return createCustomCourse(customCourseListRequestDto);
        } catch (IllegalArgumentException e) {

            return CustomCourseListResponseDto.withError("CustomCourse update failed: " + e.getMessage());
        } catch (Exception e) {

            return CustomCourseListResponseDto.withError("Server error occurred while updating CustomCourse: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteCustomCourse(Long courseId) {
        CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();
        UserEntity currentUser = userRepository.findByUsername(customUser.getUsername());

        try {
            Optional<CustomCourseCommonEntity> foundCustomCourses = customCourseRepository.findById(courseId);
            if (foundCustomCourses.isEmpty()) {
                throw new IllegalArgumentException("No CustomCourse exists for Course ID (" + courseId + ")");
            }

            CustomCourseCommonEntity common = foundCustomCourses.get();
            if (!common.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("Insufficient permission to delete");
            }

            customCourseRepository.delete(common);

            return "CustomCourse deletion successful. Course ID: " + courseId;
        } catch (IllegalArgumentException e) {

            return "CustomCourse deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deleting CustomCourse: " + e.getMessage();
        }
    }

}