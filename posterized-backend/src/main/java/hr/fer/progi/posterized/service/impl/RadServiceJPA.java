package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.RadRepository;
import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.OsobaService;
import hr.fer.progi.posterized.service.KonferencijaService;
import hr.fer.progi.posterized.service.RadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

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
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija has already finished");

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
        } else if(konf.getRadovi().stream().anyMatch(rad2 -> rad2.getAutor().getEmail().equals(autor.getEmail()))){
            Assert.hasText("","You are already registered for this conference with one Rad.");
        }
        rad.setAutor(osoba);
        rad.setKonferencija(konf);

        Media objekt = new Media();
        rad.setUrlPoster(objekt.upload(poster, UUID.randomUUID().toString(), nazivKonf+"/posteri"));
        rad.setNazivPoster(objekt.getFileName());
        if(!pptx.isEmpty()){
            rad.setUrlPptx(objekt.upload(pptx, UUID.randomUUID().toString(), nazivKonf+"/pptx"));
            rad.setNazivPptx(objekt.getFileName());
        }
        konf.getRadovi().add(rad);
        osoba.getRadovi().add(rad);
        radRepo.save(rad);
    }

    @Override
    @Transactional
    public void izbrisiRad(String admin, String naslov){
        Rad rad = radRepo.findByNaslovIgnoreCase(naslov);
        if(rad == null) Assert.hasText("","Rad with naslov " + naslov + " does not exists");
        Konferencija konf = rad.getKonferencija();
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");
        if(konf.getVrijemePocetka() != null && konf.getVrijemePocetka().after(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija has already started");
        konf.getRadovi().remove(rad);
        rad.getAutor().getRadovi().remove(rad);

        Media objekt = new Media();
        objekt.deleteFile(rad.getNazivPoster(), konf.getNaziv()+"/posteri");
        if(rad.getUrlPptx() != null) objekt.deleteFile(rad.getNazivPptx(), konf.getNaziv()+"/pptx");
        radRepo.deleteByNaslovIgnoreCase(naslov);
    }

    @Override
    public Rad findByNaslovIgnoreCase(String naslov) {
        return radRepo.findByNaslovIgnoreCase(naslov);
    }

    @Override
    public void plasman(String naziv) {
        Konferencija konf = konfService.findByNazivIgnoreCase(naziv);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + naziv + " does not exists");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija hasn't started yet");

        Set<Rad> radovi = konf.getRadovi();
        List<Rad> radoviList = new ArrayList<>(radovi);
        radoviList.sort(Comparator.comparing(Rad::getUkupnoGlasova).reversed());
        int lastPlace = 1;
        for (int i = 0; i < radoviList.size(); i++) {
            Rad rad = radoviList.get(i);
            Integer glasovi = rad.getUkupnoGlasova();
            if (i > 0 && (radoviList.get(i - 1).getUkupnoGlasova() > glasovi)) {
                lastPlace = i + 1;
                rad.setPlasman(lastPlace);

            } else rad.setPlasman(lastPlace);
            radRepo.save(rad);
        }
    }


}
