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
            Assert.hasText("","Rad s naslovom '" + rad.getNaslov() + "' već postoji.");
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
        if(rad == null) Assert.hasText("","Rad s naslovom '" + naslov + "' ne postoji.");
        Konferencija konf = rad.getKonferencija();
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        //if(konf.getVrijemePocetka() != null && konf.getVrijemePocetka().before(new Timestamp(System.currentTimeMillis())))
            //Assert.hasText("","Konferencija je već počela.");
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

    @Override
    public void updateRad(String admin, String stariNazivRad, String nazivRad, String ime, String prezime, String email, MultipartFile poster, MultipartFile pptx, String nazivKonf) {
        Rad rad = radRepo.findByNaslovIgnoreCase(stariNazivRad);
        Assert.notNull(rad,"Rad ne postoji.");
        if(!rad.getKonferencija().getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        if(!nazivRad.isEmpty() && !nazivRad.equalsIgnoreCase(stariNazivRad) && (radRepo.countByNaslovIgnoreCase(nazivRad) == 0) )
            rad.setNaslov(nazivRad);
        else if (!nazivRad.equalsIgnoreCase(stariNazivRad) && (radRepo.countByNaslovIgnoreCase(nazivRad) > 0))
            Assert.hasText("", "Već postoji rad s ovim nazivom.");
        if((!email.isEmpty() && !email.equalsIgnoreCase(rad.getAutor().getEmail())) || (!ime.isEmpty() && !ime.equalsIgnoreCase(rad.getAutor().getIme())) || (!prezime.isEmpty() && !prezime.equalsIgnoreCase(rad.getAutor().getPrezime()))) {
            Osoba noviAutor = oService.findByEmail(email);

            if (noviAutor == null) {
                noviAutor = new Osoba();
                noviAutor.setEmail(email);
                noviAutor.setIme(ime);
                noviAutor.setPrezime(prezime);
                noviAutor.setUloga("autor");
                noviAutor.getRadovi().add(rad);
                Osoba osoba = oService.createAutor(noviAutor);
                rad.setAutor(osoba);
            } else {
                Osoba finalNoviAutor = noviAutor;
                if(rad.getKonferencija().getRadovi().stream().anyMatch(rad2 -> (rad2.getAutor().getEmail().equals(finalNoviAutor.getEmail()) && !rad2.getNaslov().equalsIgnoreCase(nazivRad)))){
                    Assert.hasText("","Na navedenoj konferenciji već postoji rad ovog autora.");
                } else if(noviAutor.getEmail().equals(rad.getKonferencija().getAdminKonf().getEmail())){
                    Assert.hasText("","Na navedenoj konferenciji navedeni autor je ujedno i admin te stoga ne može prijaviti svoj rad.");
                }
                if (!email.isEmpty() && !email.equalsIgnoreCase(rad.getAutor().getEmail())){
                    rad.getAutor().getRadovi().remove(rad);
                    rad.setAutor(noviAutor);
                    noviAutor.getRadovi().add(rad);
                }
                if (((!ime.isEmpty() && !ime.equalsIgnoreCase(rad.getAutor().getIme())) || (!prezime.isEmpty() && !prezime.equalsIgnoreCase(rad.getAutor().getPrezime()))) && rad.getAutor().getEmail().equals(email)){
                    if (noviAutor.getUloga().equals("autor")){
                        rad.getAutor().setIme(ime);
                        rad.getAutor().setPrezime(prezime);
                    } else {
                        Assert.hasText("", "Ne možete promijeniti podatke za ovog korisnika.");
                    }
                }
            };
        }
        if(poster != null && !poster.isEmpty()) {
            Media objekt = new Media();
            if (!stariNazivRad.equalsIgnoreCase(nazivRad))
                objekt.deleteFile(rad.getNazivPoster(), rad.getKonferencija().getNaziv()+"/posteri");
            rad.setUrlPoster(objekt.upload(poster, rad.getNaslov(), rad.getKonferencija().getNaziv()+"/posteri"));
            rad.setNazivPoster(objekt.getFileName());
        } else if (!nazivRad.isEmpty() && !nazivRad.equalsIgnoreCase(stariNazivRad) && (rad.getUrlPoster() != null)){
            Media objekt = new Media();
            rad.setUrlPoster(objekt.changeFileNamePdf(stariNazivRad, nazivRad, rad.getKonferencija().getNaziv()+"/posteri"));
            int index = rad.getUrlPoster().lastIndexOf("/");
            int index2 = rad.getUrlPoster().lastIndexOf("?");
            String naziv = rad.getUrlPoster().substring(index+rad.getKonferencija().getNaziv().length()+14, index2);
            rad.setNazivPoster(naziv);
        }
        if(pptx != null && !pptx.isEmpty()){
            Media objekt = new Media();
            if (!stariNazivRad.equalsIgnoreCase(nazivRad))
                objekt.deleteFile(rad.getNazivPptx(), rad.getKonferencija().getNaziv()+"/pptx");
            rad.setUrlPptx(objekt.upload(pptx, rad.getNaslov(), rad.getKonferencija().getNaziv()+"/pptx"));
            rad.setNazivPptx(objekt.getFileName());
        } else if (!nazivRad.isEmpty() && !nazivRad.equalsIgnoreCase(stariNazivRad) && (rad.getUrlPptx() != null)){
            Media objekt = new Media();
            rad.setUrlPptx(objekt.changeFileNamePptx(stariNazivRad, nazivRad, rad.getKonferencija().getNaziv()+"/pptx"));
            int index = rad.getUrlPptx().lastIndexOf("/");
            int index2 = rad.getUrlPptx().lastIndexOf("?");
            String naziv = rad.getUrlPptx().substring(index+rad.getKonferencija().getNaziv().length()+11, index2);
            rad.setNazivPptx(naziv);
        }

        if(!nazivKonf.isEmpty() && !nazivKonf.equalsIgnoreCase(rad.getKonferencija().getNaziv())){
            Konferencija konf = konfService.findByNazivIgnoreCase(nazivKonf);
            if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
                Assert.hasText("","Konferencija je već završila.");
            if (konf.getRadovi().stream().anyMatch(rad2 -> rad2.getAutor().getEmail().equals(rad.getAutor().getEmail()))){
                Assert.hasText("","Na navedenoj konferenciji već postoji rad ovog autora.");
            } else if(email.equals(konf.getAdminKonf().getEmail())){
                Assert.hasText("","Na navedenoj konferenciji navedeni autor je ujedno i admin te stoga ne može prijaviti svoj rad.");
            }
            Media objekt = new Media();
            rad.setUrlPoster(objekt.changeFilePlace(rad.getNazivPoster(), rad.getKonferencija().getNaziv()+"/posteri", nazivKonf+"/posteri"));
            rad.setUrlPptx(objekt.changeFilePlace(rad.getNazivPptx(), rad.getKonferencija().getNaziv()+"/pptx", nazivKonf+"/pptx"));
            rad.getKonferencija().getRadovi().remove(rad);
            rad.setKonferencija(konf);
            konf.getRadovi().add(rad);
        }
        radRepo.save(rad);
    }

}
