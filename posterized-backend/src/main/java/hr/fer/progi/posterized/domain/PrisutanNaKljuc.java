package hr.fer.progi.posterized.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PrisutanNaKljuc implements Serializable {

    @Column(name = "konf_id")
    private Long konfId;

    @Column(name = "korisnik_id")
    private Long korisnikId;

    public Long getKonfId() {
        return konfId;
    }

    public void setKonfId(Long konfId) {
        this.konfId = konfId;
    }

    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }
}
