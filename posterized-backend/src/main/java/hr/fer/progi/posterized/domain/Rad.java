package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rad")
public class Rad {
    @Id @GeneratedValue
    private Long id;
    private String naslov;
    private String urlPptx;
    private String urlPoster;
    private Integer ukupnoGlasova;
    public Rad() {
        this.ukupnoGlasova = 0;
    }
    @ManyToOne()
    @JoinColumn(name = "konf_id")
    private Konferencija konferencija;

    @ManyToOne()
    @JoinColumn(name = "autor_id")
    private Osoba autor;
    public String getUrlPptx() {
        return urlPptx;
    }

    public void setUrlPptx(String urlPptx) {
        this.urlPptx = urlPptx;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public Integer getUkupnoGlasova() {
        return ukupnoGlasova;
    }

    public void setUkupnoGlasova(Integer ukupnoGlasova) {
        this.ukupnoGlasova = ukupnoGlasova;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public Konferencija getKonferencija() {
        return konferencija;
    }
    public void setKonferencija(Konferencija konferencija) {
        this.konferencija = konferencija;
    }

    public Osoba getAutor() {
        return autor;
    }

    public void setAutor(Osoba osoba) {
        this.autor = osoba;
    }
}
