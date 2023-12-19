package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Mjesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MjestoRepository extends JpaRepository<Mjesto,Long> {

    public Mjesto findByPbr(Integer pbr);
    public int countByPbr(Integer pbr);

}
