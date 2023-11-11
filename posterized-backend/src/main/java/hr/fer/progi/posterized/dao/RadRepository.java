package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Rad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadRepository extends JpaRepository<Rad,Long> {
    int countByNaslov(String imeRada) ;
}
