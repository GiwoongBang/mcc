package kr.co.mountaincc.users.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Boolean existsByUsername(String username);

    @Transactional
    void deleteAllByUsername(String username);

    Boolean existsByRefreshToken(String refreshToken);

    List<RefreshTokenEntity> findAllByUsername(String username);

    @Transactional
    void deleteByRefreshToken(String refreshToken);

}
