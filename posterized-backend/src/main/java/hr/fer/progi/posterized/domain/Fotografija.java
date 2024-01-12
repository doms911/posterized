package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "fotografija")
public class Fotografija {
    @Id
    @GeneratedValue
    private Long id;
    private String urlSlike;

    @ManyToOne()
    @JoinColumn(name = "konf_id")
    private Konferencija konferencija;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
