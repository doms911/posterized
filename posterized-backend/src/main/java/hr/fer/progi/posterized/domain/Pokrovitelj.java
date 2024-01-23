package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="pokrovitelj")

public class Pokrovitelj {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String url;
    @Column(unique = true)
    private String naziv;
    private String urlSlike;

    @ManyToMany(mappedBy = "pokrovitelji")
    Set<Konferencija> konferencije;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }

    public Set<Konferencija> getKonferencije() {
        return konferencije;
    }

    public void setKonferencije(Set<Konferencija> konferencije) {
        this.konferencije = konferencije;
    }
}
