package hr.fer.progi.posterized.service;
import hr.fer.progi.posterized.domain.Osoba;

import java.util.List;

public interface AdminKorisnikService {
    List<Osoba> listAll();
    Osoba createAdminKorisnik(Osoba osoba);
}