package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.GpxRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx.GpxDetailResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.gpx.GpxListResponseDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.subCategory.SubCategoryMinimalResponseDto;
import kr.co.mountaincc.maps.entities.DetailCategoryEntity;
import kr.co.mountaincc.maps.entities.GpxEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.repositories.DetailCategoryRepository;
import kr.co.mountaincc.maps.repositories.GpxRepository;
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
public class GpxService {

    private final GpxRepository gpxRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final DetailCategoryRepository detailCategoryRepository;

    private static final String SUCCESS_MESSAGE = "성공적으로 처리됐습니다.";

    public Page<GpxListResponseDto> getAllGpxs(Pageable pageable) {
        try {
            Page<GpxEntity> allList = gpxRepository.findAll(pageable);

            List<GpxListResponseDto> resData = allList.getContent().stream()
                    .map(gpx -> GpxListResponseDto.of(gpx, SUCCESS_MESSAGE))
                    .collect(Collectors.toList());

            return new PageImpl<>(resData, pageable, allList.getTotalElements());
        } catch (Exception e) {

            return new PageImpl<>(List.of(GpxListResponseDto.withError("GPXList retrieval failed: " + e.getMessage())));
        }
    }

    public GpxDetailResponseDto getGpxDetails(Long subCategoryId) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("The SubCategory ID does not exist: " + subCategoryId));

            List<GpxListResponseDto> gpxMappings = gpxRepository.findBySubCategory(subCategory)
                    .stream()
                    .map(gpx -> GpxListResponseDto.of(gpx, SUCCESS_MESSAGE))
                    .toList();

            GpxDetailResponseDto resData = GpxDetailResponseDto.of(
                    SubCategoryMinimalResponseDto.of(subCategory),
                    gpxMappings,
                    SUCCESS_MESSAGE
            );

            return resData;
        } catch (IllegalArgumentException e) {

            return (GpxDetailResponseDto) List.of(GpxListResponseDto.withError("GPX retrieval failed: " + e.getMessage()));
        } catch (Exception e) {

            return (GpxDetailResponseDto) List.of(GpxListResponseDto.withError("Server error occurred while retrieving GPX: " + e.getMessage()));
        }
    }

    @Transactional
    public GpxListResponseDto createGpx(GpxRequestDto gpxRequestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(gpxRequestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + gpxRequestDto.getSubCategoryId()));

            DetailCategoryEntity startPoint = detailCategoryRepository.findById(gpxRequestDto.getStartPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + gpxRequestDto.getStartPointId()));

            DetailCategoryEntity endPoint = detailCategoryRepository.findById(gpxRequestDto.getEndPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + gpxRequestDto.getEndPointId()));

            GpxEntity gpx = GpxEntity.of(gpxRequestDto, subCategory, startPoint, endPoint);
            gpxRepository.save(gpx);

            return GpxListResponseDto.of(gpx, SUCCESS_MESSAGE);
        } catch (ClassCastException e) {

            return GpxListResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return GpxListResponseDto.withError("GPX addition failed: " + e.getMessage());
        } catch (Exception e) {

            return GpxListResponseDto.withError("Server error occurred while adding GPX: " + e.getMessage());
        }
    }

    @Transactional
    public GpxListResponseDto updateGpx(Long id, GpxRequestDto gpxRequestDto) {
        try {
            GpxEntity gpx = gpxRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The GPX ID does not exist: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(gpxRequestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + gpxRequestDto.getSubCategoryId()));

            DetailCategoryEntity startPoint = detailCategoryRepository.findById(gpxRequestDto.getStartPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + gpxRequestDto.getStartPointId()));

            DetailCategoryEntity endPoint = detailCategoryRepository.findById(gpxRequestDto.getEndPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + gpxRequestDto.getEndPointId()));

            gpx.update(gpxRequestDto, subCategory, startPoint, endPoint, gpx.getCreatedAt());

            gpxRepository.save(gpx);

            return GpxListResponseDto.of(gpx, SUCCESS_MESSAGE);
        } catch (ClassCastException e) {

            return GpxListResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return GpxListResponseDto.withError("GPX update failed: " + e.getMessage());
        } catch (Exception e) {

            return GpxListResponseDto.withError("Server error occurred while updating GPX: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteGpx(Long id) {
        try {
            GpxEntity gpx = gpxRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The GPX ID does not exist: " + id));

            gpxRepository.delete(gpx);

            return "GPX deletion successful. GPX ID: " + id;
        } catch (IllegalArgumentException e) {

            return "GPX deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deletion GPX: " + e.getMessage();
        }
    }

}