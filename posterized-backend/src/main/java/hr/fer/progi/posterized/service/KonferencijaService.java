package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Konferencija;

import java.util.List;
import java.util.Map;

public interface KonferencijaService {
    List<Konferencija> listAll();
    List<Konferencija> prikazAdmin(String email);
    void createKonferencija(String pin, String email, String naziv);
    void izbrisiKonf(String naziv);
    void zavrsiKonferencija(String admin, String nazivKonf);

    String dohvatiMjesto(Integer pin);
    String dohvatiVideo(Integer pin);

    Konferencija findByNazivIgnoreCase(String naziv);

    void saljiMail(String naziv, String vrijeme, String lokacija);

    void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjesto, String pbr, String adresa, List<String> sponzori);
    int countByPin(Integer pin);
    Konferencija findByPin(Integer pin);
    List<Map<String, String>> rezultati(Integer pin);
}
