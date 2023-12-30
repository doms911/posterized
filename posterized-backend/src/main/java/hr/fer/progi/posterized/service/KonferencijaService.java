package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Konferencija;

import java.util.List;
import java.util.Map;

public interface KonferencijaService {
    List<Konferencija> listAll();
    List<Konferencija> prikazAdmin(String email);
    Konferencija createKonferencija(Integer pin, String email, String naziv);
    void izbrisiKonf(String naziv);
    boolean zapocniKonferencija(Integer pin);
    boolean zavrsiKonferencija(Integer pin);

    String dohvatiMjesto(Integer pin);
    String dohvatiVideo(Integer pin);

    Konferencija findByNazivIgnoreCase(String naziv);
    void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjesto, String pbr, List<String> sponzori);
    int countByPin(Integer pin);
    Konferencija findByPin(Integer pin);
    List<Map<String, String>> pobjednici(Integer pin);
}
