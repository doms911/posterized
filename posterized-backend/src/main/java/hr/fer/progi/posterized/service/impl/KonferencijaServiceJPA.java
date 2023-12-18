package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.KonferencijaRepository;
import hr.fer.progi.posterized.domain.Konferencija;
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


    @Override
    public List<Konferencija> listAll(){
        return konferencijaRepo.findAll();
    }
    @Override
    public Konferencija provjeriPin(Integer pin) {
        if (konferencijaRepo.countByPin(pin) == 0){
            Assert.hasText("","Konferencija does not exist.");
        }
        return konferencijaRepo.findByPin(pin);
    }

    @Override
    public Konferencija createKonferencija(Integer pin) {
        Assert.notNull(pin, "Pin must be given.");
        Konferencija konferencija = new Konferencija();
        konferencija.setPin(pin);
        return konferencija;
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
