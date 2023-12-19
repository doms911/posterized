package hr.fer.progi.posterized.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="mjesto")
public class Mjesto {

    @Id
    private Integer pbr;
    private String naziv;

    public Integer getPbr() {
        return pbr;
    }

    public void setPbr(Integer pbr) {
        this.pbr = pbr;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
