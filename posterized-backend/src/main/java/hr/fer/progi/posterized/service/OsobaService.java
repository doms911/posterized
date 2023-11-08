package hr.fer.progi.posterized.service;
import hr.fer.progi.posterized.domain.Osoba;

import java.util.List;

public interface OsobaService {
    List<Osoba> listAll();
    Osoba createOsoba(Osoba osoba);
}
