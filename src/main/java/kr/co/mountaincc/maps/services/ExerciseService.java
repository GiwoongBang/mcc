package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.ExerciseRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.ExerciseResponseDto;
import kr.co.mountaincc.maps.entities.ExerciseEntity;
import kr.co.mountaincc.maps.entities.UserEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.repositories.ExerciseRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private static final String SUCCESS_MESSAGE = null;

    public Page<ExerciseResponseDto> getAllExercises(Pageable pageable) {
        try {
            CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();
            UserEntity currentUser = userRepository.findByUsername(customUser.getUsername());
            Long userId = currentUser.getId();

            Page<ExerciseEntity> allList = exerciseRepository.findAll(pageable);

            List<ExerciseResponseDto> resData = allList.getContent()
                    .stream()
                    .map(exercise -> ExerciseResponseDto.of(exercise, userId, SUCCESS_MESSAGE))
                    .collect(Collectors.toList());

            return new PageImpl<>(resData, pageable, allList.getTotalElements());
        } catch (Exception e) {

            return new PageImpl<>(List.of(ExerciseResponseDto.withError("ExerciseList retrieval failed: " + e.getMessage())));
        }
    }

    public ExerciseResponseDto getExerciseDetails(Long id) {
        try {
            userService.getOAuth2UserInfo();

            ExerciseEntity exercise = exerciseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The Exercise ID does not exist: " + id));
            Long userId = exercise.getUser().getId();

            ExerciseResponseDto resData = ExerciseResponseDto.of(exercise, userId, SUCCESS_MESSAGE);

            return resData;
        } catch (IllegalArgumentException e) {

            return ExerciseResponseDto.withError("Exercise retrieval failed: " + e.getMessage());
        } catch (Exception e) {

            return ExerciseResponseDto.withError("Server error occurred while retrieving Exercise: " + e.getMessage());
        }
    }

    @Transactional
    public ExerciseResponseDto createExercise(ExerciseRequestDto exerciseRequestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(exerciseRequestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + exerciseRequestDto.getSubCategoryId()));

            CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();
            UserEntity currentUser = userRepository.findByUsername(customUser.getUsername());

            if (currentUser == null) {
                throw new IllegalArgumentException("User does not exist: " + customUser.getUsername());
            }

            ExerciseEntity exercise = ExerciseEntity.of(exerciseRequestDto, currentUser, subCategory);
            exerciseRepository.save(exercise);

            ExerciseResponseDto resData = ExerciseResponseDto.of(exercise, currentUser.getId(), SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return ExerciseResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return ExerciseResponseDto.withError("Exercise addition failed: " + e.getMessage());
        } catch (Exception e) {

            return ExerciseResponseDto.withError("Server error occurred while adding Exercise: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteExercise(Long id) {
        try {
            ExerciseEntity exercise = exerciseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The Exercise ID does not exist: " + id));

            CustomOAuth2UserEntity customUser = userService.getOAuth2UserInfo();
            UserEntity currentUser = userRepository.findByUsername(customUser.getUsername());

            if (!exercise.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("Insufficient permission to delete.");
            }

            exerciseRepository.delete(exercise);

            return "Exercise deletion successful. Exercise ID: " + id;
        } catch (IllegalArgumentException e) {

            return "Exercise deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deleting Exercise: " + e.getMessage();
        }
    }

}