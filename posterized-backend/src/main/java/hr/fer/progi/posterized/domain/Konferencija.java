package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "konferencija")
public class Konferencija {
    @Id
    @GeneratedValue
    private Long id;

    private String urlVideo;

    @Column(unique = true)
    private Integer pin;

    private Timestamp vrijemePocetka;
    private Timestamp vrijemeKraja;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Timestamp getVrijemePocetka() {
        return vrijemePocetka;
    }

    public void setVrijemePocetka(Timestamp vrijemePocetka) {
        this.vrijemePocetka = vrijemePocetka;
    }

    public Timestamp getVrijemeKraja() {
        return vrijemeKraja;
    }

    public void setVrijemeKraja(Timestamp vrijemeKraja) {
        this.vrijemeKraja = vrijemeKraja;
    }

}
