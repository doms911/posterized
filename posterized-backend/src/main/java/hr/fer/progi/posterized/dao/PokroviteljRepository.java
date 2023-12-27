package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Pokrovitelj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokroviteljRepository extends JpaRepository<Pokrovitelj,Long> {
    int countByNaziv(String naziv);
}
