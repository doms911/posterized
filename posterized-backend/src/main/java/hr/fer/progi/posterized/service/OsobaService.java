package hr.fer.progi.posterized.service;
import hr.fer.progi.posterized.domain.Osoba;

import java.util.List;

public interface OsobaService {
    List<Osoba> listAll();
    Osoba createAdminKorisnik(Osoba osoba);
    Osoba findByEmail(String email);
    Integer countByEmail(String email);
    void promijeniOsobiLozinku(Osoba osoba, String lozinka, String token);
    void saljiMail(Osoba osoba, String lozinka);
    Osoba createAutor(Osoba osoba);
}
