package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
@Table (name = "rad")
public class Rad {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String naslov;

    private String nazivPptx;
    private String nazivPoster;
    private Integer ukupnoGlasova;

    public String getNazivPptx() {
        return nazivPptx;
    }

    public void setNazivPptx(String nazivPptx) {
        this.nazivPptx = nazivPptx;
    }

    public String getNazivPoster() {
        return nazivPoster;
    }

    public void setNazivPoster(String nazivPoster) {
        this.nazivPoster = nazivPoster;
    }

    public Integer getUkupnoGlasova() {
        return ukupnoGlasova;
    }

    public void setUkupnoGlasova(Integer ukupnoGlasova) {
        this.ukupnoGlasova = ukupnoGlasova;
    }

    @OneToOne
    private Osoba autor;

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

    public Osoba getAutor() {
        return autor;
    }

    public void setAutor(Osoba osoba) {
        this.autor = autor;
    }
}
