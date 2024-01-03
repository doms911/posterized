package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "fotografija")
public class Fotografija {
    @Id
    @GeneratedValue
    private Integer id;
    private String urlSlike;

    @ManyToOne()
    @JoinColumn(name = "konf_id")
    private Konferencija konferencija;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }

    public Konferencija getKonferencija() {
        return konferencija;
    }

    public void setKonferencija(Konferencija konferencija) {
        this.konferencija = konferencija;
    }
}
