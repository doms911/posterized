package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.RadRepository;
import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.Rad;
import hr.fer.progi.posterized.service.OsobaService;
import hr.fer.progi.posterized.service.KonferencijaService;
import hr.fer.progi.posterized.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import hr.fer.progi.posterized.domain.Media;

import java.util.List;
import java.util.UUID;

@Service
public class RadServiceJPA implements RadService {

    @Autowired
    private RadRepository radRepo;
    @Autowired
    private KonferencijaService konfService;
    @Autowired
    private OsobaService oService;
    @Override
    public List<Rad> listAll() {
        return radRepo.findAll();
    }


    @Override
    public void createRad(String admin, Osoba autor, Rad rad, MultipartFile poster, MultipartFile pptx, String nazivKonf) {
        Konferencija konf = konfService.findByNazivIgnoreCase(nazivKonf);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + nazivKonf + " does not exists");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");

        Assert.notNull(autor, "Autor must be given");
        Assert.hasText(autor.getIme(), "Ime autora must be given");
        Assert.hasText(autor.getPrezime(), "Prezime autora must be given");
        Assert.hasText(autor.getEmail(), "Email autora must be given");

        if(poster.isEmpty())Assert.notNull(poster,"Poster must be given");

        Assert.notNull(rad, "Rad must be given");
        Assert.hasText(rad.getNaslov(), "Naslov must be given");
        if (radRepo.countByNaslovIgnoreCase(rad.getNaslov()) > 0){
            Assert.hasText("","Rad with name " + rad.getNaslov() + " already exits");
        }

        Osoba osoba = oService.findByEmail(autor.getEmail());
        if(osoba == null) {
            osoba = oService.createAutor(autor);
        }
        rad.setAutor(osoba);
        rad.setKonferencija(konf);

        Media objekt = new Media();
        rad.setUrlPoster(objekt.upload(poster, UUID.randomUUID().toString(), nazivKonf+"/posteri"));
        if(!pptx.isEmpty())rad.setUrlPptx(objekt.upload(pptx, UUID.randomUUID().toString(), nazivKonf+"/pptx"));
        konf.getRadovi().add(rad);
        osoba.getRadovi().add(rad);
        radRepo.save(rad);
    }
}
