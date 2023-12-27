package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.KonferencijaRepository;
import hr.fer.progi.posterized.dao.OsobaRepository;
import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;


@Service
public class KonferencijaServiceJPA implements KonferencijaService {

    @Autowired
    private KonferencijaRepository konferencijaRepo;
    @Autowired
    private OsobaRepository osobaRepo;
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";

    @Override
    public List<Konferencija> listAll(){
        return konferencijaRepo.findAll();
    }

    @Override
    public List<Konferencija> prikazAdmin(String email){
        Osoba osoba = osobaRepo.findByEmail(email);
        return konferencijaRepo.findAllByAdminKonf_id(osoba.getId());
    };
    @Override
    public Konferencija provjeriPin(Integer pin) {
        if (konferencijaRepo.countByPin(pin) == 0){
            Assert.hasText("","Konferencija does not exist.");
        }
        return konferencijaRepo.findByPin(pin);
    }

    @Override
    public Konferencija createKonferencija(Integer pin, String email, String naziv) {
        Assert.notNull(pin, "Pin must be given.");
        if (konferencijaRepo.countByPin(pin) > 0){
            Assert.hasText("","Konferencija already exists.");
        }
        Konferencija konferencija = new Konferencija();
        Assert.hasText(naziv, "Naziv must be given");
        if (konferencijaRepo.countByNaziv(naziv) > 0){
            Assert.hasText("","Naziv already exists.");
        }
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email must be in a valid format, e.g., user@example.com, not '" + email + "'"
        );
        if (osobaRepo.countByEmail(email) == 0) {
            Assert.hasText("", "Osoba with email " + email + " does not exists");
        }
        konferencija.setPin(pin);
        konferencija.setNaziv(naziv);
        konferencija.setAdminKonf(osobaRepo.findByEmail(email));
        return konferencijaRepo.save(konferencija);
    }

    @Override
    public boolean zapocniKonferencija(Integer pin) {
        return false;
    }

    @Override
    public boolean zavrsiKonferencija(Integer pin) {
        return false;
    }

    @Override
    public Konferencija updateKonferencija(String urlVideo, Integer pin, Timestamp vrijemePocetka, Timestamp vrijemeKraja, Integer pbr, String mjesto) {
        Konferencija novaKonferencija;
        novaKonferencija = konferencijaRepo.findByPin(pin);
        novaKonferencija.setUrlVideo(urlVideo);
        novaKonferencija.setVrijemeKraja(vrijemeKraja);
        novaKonferencija.setVrijemePocetka(vrijemePocetka);
        return novaKonferencija;
    }
}
