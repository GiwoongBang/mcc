package kr.co.mountaincc.maps.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.mountaincc.maps.enums.MainCategoryType;
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
@Table(name = "mcc_map_main_category")
@Entity
public class MainCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="main_category", nullable = false)
    private MainCategoryType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoryEntity> subcategories;

    @Builder
    public MainCategoryEntity(MainCategoryType type, LocalDateTime updatedAt) {
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public static MainCategoryEntity of(MainCategoryType type) {

        return MainCategoryEntity.builder()
                .type(type)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(MainCategoryType type) {
        this.type = type;
        this.updatedAt = LocalDateTime.now();
    }

}
