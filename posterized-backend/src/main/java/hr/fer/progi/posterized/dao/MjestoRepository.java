package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Mjesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MjestoRepository extends JpaRepository<Mjesto,Long> {

    Mjesto findByPbr(Integer pbr);
    int countByPbr(Integer pbr);

}
