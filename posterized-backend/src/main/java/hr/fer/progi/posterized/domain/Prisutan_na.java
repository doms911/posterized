package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "prisutan_na")
public class Prisutan_na {
    @EmbeddedId
    private PrisutanNaKljuc id;

    @ManyToOne
    @MapsId("konfId")
    @JoinColumn(name = "konf_id")
    private Konferencija konferencija;

    @ManyToOne
    @MapsId("korisnikId")
    @JoinColumn(name = "korisnik_id")
    private Osoba korisnik;
    private boolean glasao;

    public Prisutan_na() {
        this.glasao = false;
    }
    public PrisutanNaKljuc getId() {
        return id;
    }

    public void setId(PrisutanNaKljuc id) {
        this.id = id;
    }

    public Konferencija getKonferencija() {
        return konferencija;
    }

    public void setKonferencija(Konferencija konferencija) {
        this.konferencija = konferencija;
    }

    public Osoba getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Osoba korisnik) {
        this.korisnik = korisnik;
    }

    public boolean isGlasao() {
        return glasao;
    }

    public void setGlasao(boolean glasao) {
        this.glasao = glasao;
    }
}
