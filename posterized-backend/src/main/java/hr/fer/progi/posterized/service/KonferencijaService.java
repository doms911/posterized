package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Osoba;

import java.sql.Timestamp;
import java.util.List;

public interface KonferencijaService {
    List<Konferencija> listAll();
    public Konferencija provjeriPin(Integer pin);
    public Konferencija createKonferencija(Integer pin);
    public boolean zapocniKonferencija(Integer pin);
    public boolean zavrsiKonferencija(Integer pin);

    public Konferencija updateKonferencija(String urlVideo, Integer pin, Timestamp vrijemePocetka, Timestamp vrijemeKraja, Integer pbr, String mjesto);
}
