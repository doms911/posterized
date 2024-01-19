package hr.fer.progi.posterized.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "osoba")
public class Osoba {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    //@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Column(unique = true)
    private String email;

    private String lozinka;
    @NotNull
    private String ime;
    @NotNull
    private String prezime;
    @NotNull
    private String uloga;

    @OneToMany(mappedBy = "autor", orphanRemoval = true)
    private Set<Rad> radovi = new HashSet<>();
    @OneToMany(mappedBy = "korisnik")
    private Set<Prisutan_na> prisutnost;
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
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
}
