package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Pokrovitelj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PokroviteljRepository extends JpaRepository<Pokrovitelj,Long> {
    int countByNaziv(String naziv);
    int countByUrl(String url);

    Pokrovitelj findByNaziv (String naziv);
    Pokrovitelj findByNazivIgnoreCase (String naziv);

    @Query("SELECT COUNT(p) FROM Pokrovitelj p WHERE LOWER(p.naziv) = LOWER(:naziv)")
    long countByNazivCaseInsensitive(@Param("naziv") String naziv);
}
