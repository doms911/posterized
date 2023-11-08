package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
public class Rad {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String imeRada;

    @OneToOne
    private Osoba osoba;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeRada() {
        return imeRada;
    }

    public void setImeRada(String imeRada) {
        this.imeRada = imeRada;
    }

    public Osoba getAutor() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }
}
