package kr.co.mountaincc.maps.repositories;

import kr.co.mountaincc.maps.entities.DetailCategoryEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailCategoryRepository extends JpaRepository<DetailCategoryEntity, Long> {

    List<DetailCategoryEntity> findBySubCategory(SubCategoryEntity subCategory);

}
