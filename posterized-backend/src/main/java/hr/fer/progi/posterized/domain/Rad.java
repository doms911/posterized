package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

@Entity
public class Rad {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String imeRada;

    @OneToOne
    private Korisnik autor;

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

    public Korisnik getAutor() {
        return autor;
    }

    public void setAutor(Korisnik autor) {
        this.autor = autor;
    }
}
