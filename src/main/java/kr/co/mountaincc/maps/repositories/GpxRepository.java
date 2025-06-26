package kr.co.mountaincc.maps.repositories;

import kr.co.mountaincc.maps.entities.GpxEntity;
import kr.co.mountaincc.maps.entities.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GpxRepository extends JpaRepository<GpxEntity, Long> {

    List<GpxEntity> findBySubCategory(SubCategoryEntity subCategory);

}
