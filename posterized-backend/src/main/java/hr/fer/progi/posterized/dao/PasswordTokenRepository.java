package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken,Long> {
    PasswordToken findByToken(String token);
    PasswordToken findByOsoba_id(long id);
    List<PasswordToken> deleteByToken(String token);
}
