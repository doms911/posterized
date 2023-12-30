package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.PrisutanNaKljuc;
import hr.fer.progi.posterized.domain.Prisutan_na;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PrisutanNaService {
    List<Map<String, String>> provjeriPin(String korisnik, Integer pin);
    Optional<Prisutan_na> findByPrisutanNaKljuc(PrisutanNaKljuc kljuc);
    void glasaj(String korisnik, String naslov);
    void saljiMail(String admin, String nazivKonf);
}
