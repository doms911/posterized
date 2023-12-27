package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KonferencijaRepository extends JpaRepository<Konferencija,Long>{
    public Konferencija findByPin(Integer pin);

    public int countByPin(Integer pin);
    public int countByNaziv(String naziv);
}
