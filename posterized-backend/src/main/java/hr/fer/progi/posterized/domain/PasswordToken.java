package hr.fer.progi.posterized.domain;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "PasswordToken")
public class PasswordToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @OneToOne(targetEntity = Osoba.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "osoba_id")
    private Osoba osoba;

    private Date istek;

    public PasswordToken() {
    }

    public PasswordToken(String token) {
        this.token = token;
        this.istek = izracunajIstek(EXPIRATION);
    }

    public PasswordToken(String token, Osoba osoba) {
        this.token = token;
        this.osoba = osoba;
        this.istek = izracunajIstek(EXPIRATION);
    }

    private Date izracunajIstek(int istek) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, istek);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(String token) {
        this.token = token;
        this.istek = izracunajIstek(EXPIRATION);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Date getIstek() {
        return istek;
    }

    public void setIstek(Date istek) {
        this.istek = istek;
    }

    @Override
    public String toString() {
        return "PasswordToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", osoba=" + osoba +
                ", istek=" + istek +
                '}';
    }
}