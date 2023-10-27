package hr.fer.progi.posterized.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
public class Korisnik {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    private String givenName;
    private String familyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
