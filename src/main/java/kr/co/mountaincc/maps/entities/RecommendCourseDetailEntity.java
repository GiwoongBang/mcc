package kr.co.mountaincc.maps.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "mcc_map_recommend_course_detail")
@Entity
public class RecommendCourseDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_common_id", referencedColumnName = "id", nullable = false)
    private RecommendCourseCommonEntity recommendCourseCommon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(name = "course_order", nullable = false)
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gpx_id", referencedColumnName = "id", nullable = false)
    private GpxEntity gpx;

    @Builder
    public RecommendCourseDetailEntity(RecommendCourseCommonEntity recommendCourseCommon,
                                       SubCategoryEntity subCategory, int order, GpxEntity gpx) {
        this.recommendCourseCommon = recommendCourseCommon;
        this.subCategory = subCategory;
        this.order = order;
        this.gpx = gpx;
    }

    public static RecommendCourseDetailEntity of(RecommendCourseCommonEntity recommendCourseCommon,
                                                 SubCategoryEntity subCategory, int order, GpxEntity gpx) {
        return RecommendCourseDetailEntity.builder()
                .recommendCourseCommon(recommendCourseCommon)
                .subCategory(subCategory)
                .order(order)
                .gpx(gpx)
                .build();
    }

}