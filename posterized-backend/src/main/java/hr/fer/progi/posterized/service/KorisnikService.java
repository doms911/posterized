package hr.fer.progi.posterized.service;
import hr.fer.progi.posterized.domain.Korisnik;

import java.util.List;

public interface KorisnikService {
    List<Korisnik> listAll();
    Korisnik createKorisnik(Korisnik korisnik);
}
