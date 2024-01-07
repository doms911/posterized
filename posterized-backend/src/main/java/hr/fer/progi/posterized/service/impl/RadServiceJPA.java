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
        if(konf == null) Assert.hasText("","Konferencija s nazivom " + nazivKonf + " ne postoji.");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija je već završila.");

        Assert.notNull(autor, "Podaci o autoru moraju biti navedeni.");
        Assert.hasText(autor.getIme(), "Ime autora mora biti navedeno.");
        Assert.hasText(autor.getPrezime(), "Prezime autora mora biti navedeno.");
        Assert.hasText(autor.getEmail(), "Email autora mora biti naveden.");

        if(poster.isEmpty())Assert.notNull(poster,"Poster mora biti učitan.");

        Assert.notNull(rad, "Podaci o radu moraju biti navedeni.");
        Assert.hasText(rad.getNaslov(), "Naslov mora biti naveden.");
        if (radRepo.countByNaslovIgnoreCase(rad.getNaslov()) > 0){
            Assert.hasText("","Rad s naslovom " + rad.getNaslov() + " već postoji.");
        }

        Osoba osoba = oService.findByEmail(autor.getEmail());
        if(osoba == null) {
            osoba = oService.createAutor(autor);
        } else if(konf.getRadovi().stream().anyMatch(rad2 -> rad2.getAutor().getEmail().equals(autor.getEmail()))){
            Assert.hasText("","Na navedenoj konferenciji već postoji rad ovog autora.");
        }else if(osoba.getEmail().equals(konf.getAdminKonf().getEmail())){
            Assert.hasText("","Na navedenoj konferenciji navedeni autor je ujedno i admin te stoga ne može prijaviti svoj rad.");
        }
        rad.setAutor(osoba);
        rad.setKonferencija(konf);

        Media objekt = new Media();
        rad.setUrlPoster(objekt.upload(poster, rad.getNaslov(), nazivKonf+"/posteri"));
        rad.setNazivPoster(objekt.getFileName());
        if(pptx != null && !pptx.isEmpty()){
            rad.setUrlPptx(objekt.upload(pptx, rad.getNaslov(), nazivKonf+"/pptx"));
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
        if(rad == null) Assert.hasText("","Rad s naslovom " + naslov + " ne postoji.");
        Konferencija konf = rad.getKonferencija();
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        if(konf.getVrijemePocetka() != null && konf.getVrijemePocetka().after(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija je već počela.");
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
        if(konf == null) Assert.hasText("","Konferencija s nazivom " + naziv + " ne postoji.");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija još nije počela.");

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
