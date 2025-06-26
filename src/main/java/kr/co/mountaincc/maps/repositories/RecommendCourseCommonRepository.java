package kr.co.mountaincc.maps.repositories;

import kr.co.mountaincc.maps.entities.RecommendCourseCommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendCourseCommonRepository extends JpaRepository<RecommendCourseCommonEntity, Long> {

    Optional<RecommendCourseCommonEntity> findById(Long id);

}
