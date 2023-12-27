package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;


import java.sql.Timestamp;
import java.util.Set;

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

    @ManyToOne
    private Osoba adminKonf;
    @ManyToOne
    private Mjesto mjesto;

    public Osoba getAdminKonf() {
        return adminKonf;
    }

    public void setAdminKonf(Osoba adminKonf) {
        this.adminKonf = adminKonf;
    }

    public Mjesto getMjesto() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }

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
