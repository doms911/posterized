package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Osoba;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface KonferencijaService {
    List<Konferencija> listAll();
    List<Konferencija> prikazAdmin(String email);
    Konferencija provjeriPin(Integer pin);
    Konferencija createKonferencija(Integer pin, String email, String naziv);
    void izbrisiKonf(String admin, String naziv);
    boolean zapocniKonferencija(Integer pin);
    boolean zavrsiKonferencija(Integer pin);
    void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjesto, String pbr, List<String> sponzori);
}
