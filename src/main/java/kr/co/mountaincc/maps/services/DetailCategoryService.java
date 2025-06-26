package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.DetailCategoryRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryDetailResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryListResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.detailCategory.DetailCategorySingleResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.DetailCategoryEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.enums.DetailCategoryType;
import kr.co.mountaincc.maps.repositories.DetailCategoryRepository;
import kr.co.mountaincc.maps.repositories.SubCategoryRepository;
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
public class DetailCategoryService {

    private final DetailCategoryRepository detailCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    private static final String SUCCESS_MESSAGE = "성공적으로 처리됐습니다.";

    public Page<DetailCategoryListResponseDto> getAllDetailCategories(Pageable pageable) {
        try {
            Page<DetailCategoryEntity> allList = detailCategoryRepository.findAll(pageable);

            List<DetailCategoryListResponseDto> detailCategories = allList.getContent().stream()
                    .map(detailCategory -> DetailCategoryListResponseDto.of(detailCategory, SUCCESS_MESSAGE))
                    .collect(Collectors.toList());

            return new PageImpl<>(detailCategories, pageable, allList.getTotalElements());
        } catch (Exception e) {

            return new PageImpl<>(List.of(DetailCategoryListResponseDto.withError("DetailCategoryList retrieval failed: " + e.getMessage())));
        }
    }

    @Transactional(readOnly = true)
    public DetailCategoryDetailResponseDto getDetailCategoryDetails(Long subCategoryId) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("The SubCategory ID does not exist: " + subCategoryId));

            List<DetailCategoryMinimalResponseDto> detailCategories = detailCategoryRepository.findBySubCategory(subCategory)
                    .stream()
                    .map(DetailCategoryMinimalResponseDto::of)
                    .toList();

            DetailCategoryDetailResponseDto resData = DetailCategoryDetailResponseDto.of(
                    SubCategoryMinimalResponseDto.of(subCategory),
                    detailCategories, SUCCESS_MESSAGE);

            return resData;
        } catch (IllegalArgumentException e) {

            return DetailCategoryDetailResponseDto.withError("DetailCategory retrieval failed: " + e.getMessage());
        } catch (Exception e) {

            return DetailCategoryDetailResponseDto.withError("Server error occurred while retrieving DetailCategory: " + e.getMessage());
        }
    }

    @Transactional
    public DetailCategorySingleResponseDto createDetailCategory(DetailCategoryRequestDto requestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            DetailCategoryType type = DetailCategoryType.fromType(requestDto.getType());
            DetailCategoryEntity detailCategory = DetailCategoryEntity.of(requestDto, subCategory, type);

            detailCategoryRepository.save(detailCategory);

            DetailCategorySingleResponseDto resData = DetailCategorySingleResponseDto.of(
                    SubCategoryMinimalResponseDto.of(subCategory),
                    DetailCategoryMinimalResponseDto.of(detailCategory),
                    SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return DetailCategorySingleResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return DetailCategorySingleResponseDto.withError("DetailCategory addition failed: " + e.getMessage());
        } catch (Exception e) {

            return DetailCategorySingleResponseDto.withError("Server error occurred while adding DetailCategory: " + e.getMessage());
        }
    }

    @Transactional
    public DetailCategorySingleResponseDto updateDetailCategory(Long id, DetailCategoryRequestDto detailCategoryRequestDto) {
        try {
            DetailCategoryEntity detailCategory = detailCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The DetailCategory ID does not exist: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(detailCategoryRequestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + detailCategoryRequestDto.getSubCategoryId()));

            DetailCategoryType type = DetailCategoryType.fromType(detailCategoryRequestDto.getType());

            detailCategory.update(detailCategoryRequestDto, subCategory, type);

            detailCategoryRepository.save(detailCategory);

            DetailCategorySingleResponseDto resData = DetailCategorySingleResponseDto.of(
                    SubCategoryMinimalResponseDto.of(subCategory),
                    DetailCategoryMinimalResponseDto.of(detailCategory),
                    SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return DetailCategorySingleResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return DetailCategorySingleResponseDto.withError("DetailCategory update failed: " + e.getMessage());
        } catch (Exception e) {

            return DetailCategorySingleResponseDto.withError("Server error occurred while updating DetailCategory: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteDetailCategory(Long id) {
        try {
            DetailCategoryEntity detailCategory = detailCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The DetailCategory ID does not exist: " + id));

            detailCategoryRepository.delete(detailCategory);

            return "DetailCategory deletion successful. DetailCategory ID: " + id;
        } catch (IllegalArgumentException e) {

            return "DetailCategory deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deleting DetailCategory: " + e.getMessage();
        }
    }

}