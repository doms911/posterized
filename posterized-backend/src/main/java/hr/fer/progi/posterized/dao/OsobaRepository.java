package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Osoba;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OsobaRepository extends JpaRepository<Osoba,Long> {
    int countByEmail(String email);
    Osoba findByEmail(String email);
}
