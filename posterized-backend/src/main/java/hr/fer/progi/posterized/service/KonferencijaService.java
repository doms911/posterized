package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Osoba;

import java.sql.Timestamp;
import java.util.List;

public interface KonferencijaService {
    List<Konferencija> listAll();
    List<Konferencija> prikazAdmin(String email);
    public Konferencija provjeriPin(Integer pin);
    public Konferencija createKonferencija(Integer pin, String email, String naziv);
    public boolean zapocniKonferencija(Integer pin);
    public boolean zavrsiKonferencija(Integer pin);

    public Konferencija updateKonferencija(String urlVideo, Integer pin, Timestamp vrijemePocetka, Timestamp vrijemeKraja, Integer pbr, String mjesto);
}
