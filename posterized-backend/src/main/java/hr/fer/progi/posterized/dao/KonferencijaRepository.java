package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KonferencijaRepository extends JpaRepository<Konferencija,Long>{
    Konferencija findByPin(Integer pin);
    Konferencija findByNaziv(String naziv);
    Konferencija findByNazivIgnoreCase(String naziv);
    List<Konferencija> findAllByAdminKonf_id(Long id);
    int countByPin(Integer pin);
    int countByNaziv(String naziv);
    @Query("SELECT COUNT(p) FROM Pokrovitelj p WHERE LOWER(p.naziv) = LOWER(:naziv)")
    long countByNazivCaseInsensitive(@Param("naziv") String naziv);
}
