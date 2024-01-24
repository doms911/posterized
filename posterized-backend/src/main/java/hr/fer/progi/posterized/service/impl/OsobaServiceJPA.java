package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.OsobaRepository;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class OsobaServiceJPA implements OsobaService {

    @Autowired
    private OsobaRepository osobaRepo;

    public OsobaServiceJPA() {

    }

    public OsobaServiceJPA(OsobaRepository osobaRepository) {
        this.osobaRepo = osobaRepository;
    }

    @Override
    public List<Osoba> listAll() {
        List<Osoba> korisnici = osobaRepo.findByUloga("korisnik");
        List<Osoba> admini = osobaRepo.findByUloga("admin");
        List<Osoba> svi = new ArrayList<>(korisnici);
        svi.addAll(admini);
        return svi;
    }
    @Override
    public Osoba findByEmail(String email) {
        return osobaRepo.findByEmail(email);
    }

    @Override
    public Integer countByEmail(String email) {
        return osobaRepo.countByEmail(email);
    }
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    private static final String LOZINKA_FORMAT= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    @Autowired
    private PasswordEncoder pswdEncoder;
    @Override
    @Transactional
    public void createAdminKorisnik(Osoba osoba) {
        Assert.notNull(osoba, "Podaci o korisniku moraju biti navedeni.");
        Assert.isNull(osoba.getId(),
                "ID korisnika mora imati vrijednost null, a ne " + osoba.getId() + "."
        );
        String email = osoba.getEmail();
        Assert.hasText(email, "Email mora biti naveden.");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email mora biti u ispravnom obliku, npr. user@example.com, a ne '" + email + "'."
        );
        String ime = osoba.getIme();
        Assert.hasText(ime, "Ime mora biti navedeno.");
        String prezime = osoba.getPrezime();
        Assert.hasText(prezime, "Prezime mora biti navedeno.");

        String lozinka = osoba.getLozinka();
        Assert.hasText(lozinka, "Lozinka mora biti navedena.");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Lozinka mora biti u pravilnom obliku - barem jedan broj, jedno veliko slovo, jedno malo slovo " +
                        "i mora sadržavati barem osam znakova."
        );
        String kodiranaLozinka = pswdEncoder.encode(osoba.getLozinka());

        if (osobaRepo.countByEmail(osoba.getEmail()) > 0) {
            Osoba osoba2 = osobaRepo.findByEmail(email);
            if(osoba2.getUloga().equals("autor")) {
                osoba2.setLozinka(kodiranaLozinka);
                osoba2.setIme(ime);
                osoba2.setPrezime(prezime);
                if(osoba.getUloga().equals("admin")) osoba2.setUloga("admin");
                else osoba2.setUloga("korisnik");

                //osobaRepo.save(osoba2);
                //osobaRepo.save(osoba2);

                return;
            } else if (!osoba2.getUloga().equals("admin") && osoba.getUloga().equals("admin")){
                Assert.hasText("", "Korisnik s emailom " + osoba.getEmail() + " već postoji te ga pri stvaranju konferencije možete postaviti za admina.");
                return;
            } else Assert.hasText("", "Korisnik s emailom " + osoba.getEmail() + " već postoji.");
        }
        osoba.setLozinka(kodiranaLozinka);
        osobaRepo.save(osoba);
    }

    @Override
    @Transactional
    public Osoba createAutor(Osoba osoba) {
        Assert.notNull(osoba, "Podaci o autoru moraju biti navedeni.");
        Assert.isNull(osoba.getId(),
                "ID autora mora imati vrijednost null, a ne " + osoba.getId() + "."
        );
        String email = osoba.getEmail();
        Assert.hasText(email, "Email mora biti naveden.");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email mora biti u ispravnom obliku, npr. user@example.com, a ne '" + email + "'."
        );
        if (osobaRepo.countByEmail(osoba.getEmail()) > 0) {
            Assert.hasText("", "Autor s emailom " + osoba.getEmail() + " već postoji.");
        }
        return osobaRepo.save(osoba);
    }
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void saljiMail(Osoba osoba, String lozinka){
        Assert.notNull(osoba, "Osoba ne postoji.");
        final String url = env.getProperty("send.email.link") + "/forgot-password";
        final String message = "Vaša trenutna lozinka je: " + lozinka + ", a ako ju želite promijeniti: ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(osoba.getEmail());
        email.setSubject("Vaš račun");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        mailSender.send(email);
    }
    @Override
    @Transactional
    public void promijeniOsobiLozinku(Osoba osoba, String lozinka, String token){
        Assert.hasText(lozinka, "Lozinka mora biti navedena.");
        Assert.notNull(osoba, "Osoba ne postoji.");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Lozinka mora biti u pravilnom obliku - barem jedan broj, jedno veliko slovo, jedno malo slovo " +
                        "i mora sadržavati barem osam znakova."
        );
        osoba.setLozinka(pswdEncoder.encode(lozinka));
    }
}