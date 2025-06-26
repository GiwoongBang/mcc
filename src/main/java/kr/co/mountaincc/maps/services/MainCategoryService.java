package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.responses.mainCategory.MainCategoryResponseDto;
import kr.co.mountaincc.maps.entities.MainCategoryEntity;
import kr.co.mountaincc.maps.repositories.MainCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainCategoryService {

    private final MainCategoryRepository mainCategoryRepository;

    private static final String SUCCESS_MESSAGE = "성공적으로 처리됐습니다.";

    public List<MainCategoryResponseDto> getAllMainCategories() {
        List<MainCategoryResponseDto> resData = mainCategoryRepository.findAll()
                .stream()
                .map(mainCategory -> MainCategoryResponseDto.of(mainCategory, SUCCESS_MESSAGE))
                .collect(Collectors.toList());

        return resData;
    }

    @Transactional
    public String deleteMainCategory(Long id) {
        try {
            MainCategoryEntity mainCategory = mainCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The MainCategory ID does not exist: " + id));

            mainCategoryRepository.delete(mainCategory);

            return "MainCategory deletion successful. MainCategory ID: " + id;
        } catch (IllegalArgumentException e) {

            return "MainCategory deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deleting MainCategory: " + e.getMessage();
        }
    }
}