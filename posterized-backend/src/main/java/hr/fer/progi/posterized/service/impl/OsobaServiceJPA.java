package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.OsobaRepository;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import hr.fer.progi.posterized.service.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class OsobaServiceJPA implements OsobaService {

    @Autowired
    private OsobaRepository osobaRepo;

    @Override
    public List<Osoba> listAll() {
        return osobaRepo.findAll();
    }
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    @Autowired
    private PasswordEncoder pswdEncoder;
    @Override
    public Osoba createOsoba(Osoba osoba) {  //slučaj samo za korisnika (admin i autor nisu obrađeni)
        Assert.notNull(osoba, "Osoba object must be given");
        Assert.isNull(osoba.getId(),
                "Osoba ID must be null, not" + osoba.getId()
        );
        String email = osoba.getEmail();
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email must be in a valid format, e.g., user@example.com, not '" + email + "'"
        );
        if (osobaRepo.countByEmail(osoba.getEmail()) > 0)
            throw new RequestDeniedException(
                    "Osoba with email " + osoba.getEmail() + " already exists"
            );
        String lozinka = osoba.getLozinka();
        Assert.hasText(lozinka, "Lozinka must be given");
        String kodiranaLozinka = pswdEncoder.encode(osoba.getLozinka());
        osoba.setLozinka(kodiranaLozinka);
        String ime = osoba.getIme();
        Assert.hasText(ime, "Ime must be given");
        String prezime = osoba.getPrezime();
        Assert.hasText(prezime, "Prezima must be given");
        return osobaRepo.save(osoba);
    }
}