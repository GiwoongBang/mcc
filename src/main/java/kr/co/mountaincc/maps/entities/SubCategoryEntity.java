package kr.co.mountaincc.maps.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.mountaincc.maps.converters.StringListConverter;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.SubCategoryRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_map_sub_category")
@Entity
public class SubCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id", referencedColumnName = "id", nullable = false)
    private MainCategoryEntity mainCategory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column
    private double altitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="safety_notes", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> safetyNotes;

    @Column(name="place_info")
    private String placeInfo;

    @Column(name="thumbnail_image")
    private String thumbnailImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagEntity> tags;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailCategoryEntity> detailCategories;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GpxEntity> gpx;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendCourseDetailEntity> recommendCourseDetails;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseEntity> exercises;

    @Builder
    public SubCategoryEntity(MainCategoryEntity mainCategory, String name, double lat, double lng, double altitude,
                             String description, List<String> safetyNotes, String placeInfo,
                             String thumbnailImg, LocalDateTime updatedAt) {
        this.mainCategory = mainCategory;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
        this.description = description;
        this.safetyNotes = safetyNotes;
        this.placeInfo = placeInfo;
        this.thumbnailImg = thumbnailImg;
        this.updatedAt = updatedAt;
    }

    public static SubCategoryEntity of(SubCategoryRequestDto dto, MainCategoryEntity mainCategory) {

        return SubCategoryEntity.builder()
                .mainCategory(mainCategory)
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .altitude(dto.getAltitude())
                .description(dto.getDescription())
                .safetyNotes(dto.getSafetyNotes())
                .placeInfo(dto.getPlaceInfo())
                .thumbnailImg(dto.getThumbnailImg())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(SubCategoryRequestDto dto, MainCategoryEntity mainCategory) {
        this.mainCategory = mainCategory;
        this.name = dto.getName();
        this.lat = dto.getLat();
        this.lng = dto.getLng();
        this.altitude = dto.getAltitude();
        this.description = dto.getDescription();
        this.safetyNotes = dto.getSafetyNotes();
        this.placeInfo = dto.getPlaceInfo();
        this.thumbnailImg = dto.getThumbnailImg();
        this.updatedAt = LocalDateTime.now();
    }

}