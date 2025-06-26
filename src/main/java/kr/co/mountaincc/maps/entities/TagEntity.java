package kr.co.mountaincc.maps.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.mountaincc.maps.dtos.mapDtos.requests.TagRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_map_tag")
@Entity
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(nullable = false)
    private String tag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public TagEntity(SubCategoryEntity subCategory, String tag, LocalDateTime updatedAt) {
        this.subCategory = subCategory;
        this.tag = tag;
        this.updatedAt = updatedAt;
    }

    public static TagEntity of(TagRequestDto dto, SubCategoryEntity subCategory) {

        return TagEntity.builder()
                .subCategory(subCategory)
                .tag(dto.getTag())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(TagRequestDto dto, SubCategoryEntity subCategory) {
        this.subCategory = subCategory;
        this.tag = dto.getTag();
        this.updatedAt = LocalDateTime.now();
    }

}
