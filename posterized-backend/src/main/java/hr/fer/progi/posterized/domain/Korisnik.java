package hr.fer.progi.posterized.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Korisnik {
    @Id
    @GeneratedValue
    private Long id;
    private String mail;

    private String givenName;
    private String familyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
