package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.SubCategoryRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryResponseDto;
import kr.co.mountaincc.maps.entities.MainCategoryEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.repositories.MainCategoryRepository;
import kr.co.mountaincc.maps.repositories.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;

    private static final String SUCCESS_MESSAGE = "성공적으로 처리됐습니다.";

    public List<SubCategoryResponseDto> getAllSubCategories() {

        try {
            List<SubCategoryResponseDto> resData = subCategoryRepository.findAll()
                    .stream()
                    .map(subCategory -> SubCategoryResponseDto.of(subCategory, SUCCESS_MESSAGE))
                    .collect(Collectors.toList());

            return resData;
        } catch (Exception e) {

            return List.of(SubCategoryResponseDto.withError("SubCategoryList retrieval failed: " + e.getMessage()));
        }
    }


    @Transactional(readOnly = true)
    public SubCategoryResponseDto getSubCategoryDetails(Long subCategoryId) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("The SubCategory ID does not exist: " + subCategoryId));

            SubCategoryResponseDto resData = SubCategoryResponseDto.of(subCategory, SUCCESS_MESSAGE);

            return resData;

        } catch (IllegalArgumentException e) {

            return SubCategoryResponseDto.withError("SubCategory retrieval failed: " + e.getMessage());
        } catch (Exception e) {

            return SubCategoryResponseDto.withError("Server error occurred while retrieving SubCategory: " + e.getMessage());
        }
    }

    @Transactional
    public SubCategoryResponseDto createSubCategory(SubCategoryRequestDto subCategoryRequestDto) {
        try {
            MainCategoryEntity mainCategory = mainCategoryRepository.findById(subCategoryRequestDto.getMainCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid MainCategory ID: " + subCategoryRequestDto.getMainCategoryId()));

            SubCategoryEntity subCategory = SubCategoryEntity.of(subCategoryRequestDto, mainCategory);

            subCategoryRepository.save(subCategory);

            SubCategoryResponseDto resData = SubCategoryResponseDto.of(subCategory, SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return SubCategoryResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return SubCategoryResponseDto.withError("SubCategory addition failed: " + e.getMessage());
        } catch (Exception e) {

            return SubCategoryResponseDto.withError("Server error occurred while adding SubCategory: " + e.getMessage());
        }
    }

    @Transactional
    public SubCategoryResponseDto updateSubCategory(Long id, SubCategoryRequestDto subCategoryRequestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The SubCategory ID does not exist: " + id));

            MainCategoryEntity mainCategory = mainCategoryRepository.findById(subCategoryRequestDto.getMainCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid MainCategory ID: " + subCategoryRequestDto.getMainCategoryId()));

            subCategory.update(subCategoryRequestDto, mainCategory);

            subCategoryRepository.save(subCategory);

            SubCategoryResponseDto resData = SubCategoryResponseDto.of(subCategory, SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return SubCategoryResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return SubCategoryResponseDto.withError("SubCategory update failed: " + e.getMessage());
        } catch (Exception e) {

            return SubCategoryResponseDto.withError("Server error occurred while updating SubCategory: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteSubCategory(Long id) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The SubCategory ID does not exist: " + id));

            subCategoryRepository.delete(subCategory);

            return "SubCategory deletion successful. SubCategory ID: " + id;
        } catch (IllegalArgumentException e) {

            return "SubCategory deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deletion SubCategory: " + e.getMessage();
        }
    }

}
