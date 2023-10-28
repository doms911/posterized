package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.KorisnikRepository;
import hr.fer.progi.posterized.domain.Korisnik;
import hr.fer.progi.posterized.service.KorisnikService;
import hr.fer.progi.posterized.service.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class KorisnikServiceJPA implements KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepo;

    @Override
    public List<Korisnik> listAll() {
        return korisnikRepo.findAll();
    }
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    @Override
    public Korisnik createKorisnik(Korisnik korisnik) {
        Assert.notNull(korisnik, "Korisnik object must be given");
        Assert.isNull(korisnik.getId(),
                "Korisnik ID must be null, not" + korisnik.getId()
        );
        String email = korisnik.getEmail();
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email must be in a valid format, e.g., user@example.com, not '" + email + "'"
        );
        if (korisnikRepo.countByEmail(korisnik.getEmail()) > 0)
            throw new RequestDeniedException(
                    "Korisnik with email " + korisnik.getEmail() + " already exists"
            );
        return korisnikRepo.save(korisnik);
    }
}