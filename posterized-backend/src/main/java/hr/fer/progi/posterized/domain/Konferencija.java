package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;


import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "konferencija")
public class Konferencija {
    @Id
    @GeneratedValue
    private Long id;

    private String urlVideo;

    private String naziv;

    @Column(unique = true)
    private Integer pin;

    private Timestamp vrijemePocetka;
    private Timestamp vrijemeKraja;
    private String adresa;
    private Boolean uredeno;
    public Konferencija() {
        this.uredeno = false;
    }
    @ManyToOne
    @JoinColumn(name = "adminKonf_id")
    private Osoba adminKonf;
    @ManyToOne
    @JoinColumn(name = "pbr")
    private Mjesto mjesto;
    @OneToMany(mappedBy = "konferencija", orphanRemoval = true)
    private Set<Fotografija> fotografije = new HashSet<>();

    @OneToMany(mappedBy = "konferencija", orphanRemoval = true)
    private Set<Rad> radovi = new HashSet<>();

    @OneToMany(mappedBy = "konferencija", orphanRemoval = true)
    Set<Prisutan_na> prisutnost;
    @ManyToMany
    @JoinTable(
            name = "pokrovitelj_na",
            joinColumns = @JoinColumn(name = "konferencija_id"),
            inverseJoinColumns = @JoinColumn(name = "pokrovitelj_id"))
    Set<Pokrovitelj> pokrovitelji = new HashSet<>();


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
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Set<Pokrovitelj> getPokrovitelji() {
        return pokrovitelji;
    }

    public void setPokrovitelji(Set<Pokrovitelj> pokrovitelji) {
        this.pokrovitelji = pokrovitelji;
    }

    public Set<Fotografija> getFotografije() {
        return fotografije;
    }

    public void setFotografije(Set<Fotografija> fotografije) {
        this.fotografije = fotografije;
    }

    public Set<Rad> getRadovi() {
        return radovi;
    }

    public void setRadovi(Set<Rad> radovi) {
        this.radovi = radovi;
    }

    public Set<Prisutan_na> getPrisutnost() {
        return prisutnost;
    }

    public void setPrisutnost(Set<Prisutan_na> prisutnost) {
        this.prisutnost = prisutnost;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Boolean getUredeno() {
        return uredeno;
    }

    public void setUredeno(Boolean uredeno) {
        this.uredeno = uredeno;
    }
}
