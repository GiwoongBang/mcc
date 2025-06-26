package kr.co.mountaincc.maps.services;

import kr.co.mountaincc.maps.dtos.mapDtos.requests.TagRequestDto;
import kr.co.mountaincc.maps.dtos.mapDtos.responses.TagResponseDto;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import kr.co.mountaincc.maps.entities.TagEntity;
import kr.co.mountaincc.maps.repositories.SubCategoryRepository;
import kr.co.mountaincc.maps.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final SubCategoryRepository subCategoryRepository;

    private static final String SUCCESS_MESSAGE = null;

    public List<TagResponseDto> getAllTags() {
        try {
            List<TagResponseDto> resData = tagRepository.findAll()
                    .stream()
                    .map(tag -> TagResponseDto.of(tag, SUCCESS_MESSAGE))
                    .collect(Collectors.toList());

            return resData;
        } catch (Exception e) {

            return List.of(TagResponseDto.withError("TagList retrieval failed: " + e.getMessage()));
        }
    }

    @Transactional
    public TagResponseDto createTag(TagRequestDto requestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            TagEntity tag = TagEntity.of(requestDto, subCategory);
            tagRepository.save(tag);

            TagResponseDto resData = TagResponseDto.of(tag, SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return TagResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return TagResponseDto.withError("Tag addition failed: " + e.getMessage());
        } catch (Exception e) {

            return TagResponseDto.withError("Server error occurred while adding Tag: " + e.getMessage());
        }
    }

    @Transactional
    public TagResponseDto updateTag(Long id, TagRequestDto tagRequestDto) {
        try {
            TagEntity tag = tagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The Tag ID does not exist: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(tagRequestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + tagRequestDto.getSubCategoryId()));

            tag.update(tagRequestDto, subCategory);

            tagRepository.save(tag);

            TagResponseDto resData = TagResponseDto.of(tag, SUCCESS_MESSAGE);

            return resData;
        } catch (ClassCastException e) {

            return TagResponseDto.withError("Invalid data format: " + e.getMessage());
        } catch (IllegalArgumentException e) {

            return TagResponseDto.withError("Tag update failed: " + e.getMessage());
        } catch (Exception e) {

            return TagResponseDto.withError("Server error occurred while updating Tag: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteTag(Long id) {
        try {
            TagEntity tag = tagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("The Tag ID does not exist: " + id));

            tagRepository.delete(tag);

            return "Tag deletion successful. Tag ID: " + id;
        } catch (IllegalArgumentException e) {

            return "Tag deletion failed: " + e.getMessage();
        } catch (Exception e) {

            return "Server error occurred while deletion Tag: " + e.getMessage();
        }
    }

}