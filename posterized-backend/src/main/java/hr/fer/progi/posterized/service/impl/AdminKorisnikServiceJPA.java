package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.OsobaRepository;
import hr.fer.progi.posterized.dao.PasswordTokenRepository;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.AdminKorisnikService;
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
public class AdminKorisnikServiceJPA implements AdminKorisnikService {

    @Autowired
    private OsobaRepository osobaRepo;

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
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    private static final String LOZINKA_FORMAT= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    @Autowired
    private PasswordEncoder pswdEncoder;
    @Override
    public Osoba createAdminKorisnik(Osoba osoba) {
        Assert.notNull(osoba, "Osoba object must be given");
        Assert.isNull(osoba.getId(),
                "Osoba ID must be null, not" + osoba.getId()
        );
        String email = osoba.getEmail();
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email must be in a valid format, e.g., user@example.com, not '" + email + "'"
        );
        if (osobaRepo.countByEmail(osoba.getEmail()) > 0) {
            Assert.hasText("", "Osoba with email " + osoba.getEmail() + " already exists");
        }
        String lozinka = osoba.getLozinka();
        Assert.hasText(lozinka, "Lozinka must be given");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Password must be in a valid format, at least one number, one uppercase letter, one lowercase " +
                        "letter and at least 8 characters in length "
        );
        String kodiranaLozinka = pswdEncoder.encode(osoba.getLozinka());
        osoba.setLozinka(kodiranaLozinka);
        String ime = osoba.getIme();
        Assert.hasText(ime, "Ime must be given");
        String prezime = osoba.getPrezime();
        Assert.hasText(prezime, "Prezime must be given");
        return osobaRepo.save(osoba);
    }
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void saljiMail(Osoba osoba, String lozinka){
        final String url = env.getProperty("send.email.link") + "/forgot-password";
        final String message = "Your current password is:" + lozinka + ", if you want to change it: ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(osoba.getEmail());
        email.setSubject("Your Account");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        mailSender.send(email);
    }
    @Override
    @Transactional
    public void promijeniOsobiLozinku(Osoba osoba, String lozinka, String token){
        Assert.hasText(lozinka, "Lozinka must be given");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Password must be in a valid format, at least one number, one uppercase letter, one lowercase " +
                        "letter and at least 8 characters in length "
        );
        osoba.setLozinka(pswdEncoder.encode(lozinka));
        osobaRepo.save(osoba);
    }
}