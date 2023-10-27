package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisnikRepository extends JpaRepository<Korisnik,Long> {
}
