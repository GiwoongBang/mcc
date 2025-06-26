package kr.co.mountaincc.maps.repositories;

import kr.co.mountaincc.maps.entities.CustomCourseCommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomCourseRepository extends JpaRepository<CustomCourseCommonEntity, Long> {

    Optional<CustomCourseCommonEntity> findById(Long id);

}
