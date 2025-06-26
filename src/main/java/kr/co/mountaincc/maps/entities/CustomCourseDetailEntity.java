package kr.co.mountaincc.maps.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "mcc_map_custom_course_detail")
@Entity
public class CustomCourseDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_common_id", referencedColumnName = "id", nullable = false)
    private CustomCourseCommonEntity customCourseCommon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(name = "course_order", nullable = false)
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gpx_id", referencedColumnName = "id", nullable = false)
    private GpxEntity gpx;

    @Builder
    public CustomCourseDetailEntity(CustomCourseCommonEntity customCourseCommon,
                                    SubCategoryEntity subCategory, int order, GpxEntity gpx) {
        this.customCourseCommon = customCourseCommon;
        this.subCategory = subCategory;
        this.order = order;
        this.gpx = gpx;
    }

    public static CustomCourseDetailEntity of(CustomCourseCommonEntity customCourseCommon,
                                              SubCategoryEntity subCategory, int order, GpxEntity gpx) {
        return CustomCourseDetailEntity.builder()
                .customCourseCommon(customCourseCommon)
                .subCategory(subCategory)
                .order(order)
                .gpx(gpx)
                .build();
    }

}