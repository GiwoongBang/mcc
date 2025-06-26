package kr.co.mountaincc.maps.repositories;

import kr.co.mountaincc.maps.entities.MainCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategoryEntity, Long> {

}
