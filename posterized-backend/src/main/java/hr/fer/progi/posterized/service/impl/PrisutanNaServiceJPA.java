package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.PrisutanNaRepository;
import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.KonferencijaService;
import hr.fer.progi.posterized.service.OsobaService;
import hr.fer.progi.posterized.service.PrisutanNaService;
import hr.fer.progi.posterized.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PrisutanNaServiceJPA implements PrisutanNaService {
    @Autowired
    KonferencijaService kService;
    @Autowired
    OsobaService oService;
    @Autowired
    RadService radService;
    @Autowired
    PrisutanNaRepository prisRepo;
    @Override
    public List<Map<String, String>> provjeriPin(String korisnik, Integer pin) {
        if (kService.countByPin(pin) == 0){
            Assert.hasText("","Konferencija does not exist.");
        }
        Konferencija konf = kService.findByPin(pin);
        if(konf.getVrijemePocetka() == null || konf.getVrijemePocetka().after(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija hasn't started yet");

        if(!korisnik.equals("superadmin")){
            Osoba osoba = oService.findByEmail(korisnik);
            if(osoba == null) Assert.hasText("","Osoba does not exist.");
            PrisutanNaKljuc kljuc = new PrisutanNaKljuc();
            kljuc.setKonfId(konf.getId());
            kljuc.setKorisnikId(osoba.getId());
            if(!osoba.getEmail().equals(konf.getAdminKonf().getEmail()) && prisRepo.findById(kljuc).isEmpty()) {
                Prisutan_na zapis = new Prisutan_na();
                zapis.setId(kljuc);
                zapis.setKorisnik(osoba);
                zapis.setKonferencija(konf);
                konf.getPrisutnost().add(zapis);
                osoba.getPrisutnost().add(zapis);
                prisRepo.save(zapis);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> rezultat = new ArrayList<>();

        Map<String, String> konferencijaMapa = new HashMap<>();
        konferencijaMapa.put("naziv", konf.getNaziv());
        konferencijaMapa.put("admin", konf.getAdminKonf().getEmail());
        konferencijaMapa.put("adresa", konf.getAdresa());
        Timestamp vrijemePocetka = konf.getVrijemePocetka();
        Timestamp vrijemeKraja = konf.getVrijemeKraja();
        konferencijaMapa.put("vrijemePocetka", dateFormat.format(vrijemePocetka));
        konferencijaMapa.put("vrijemeKraja", dateFormat.format(vrijemeKraja));
        Mjesto mjesto = konf.getMjesto();
        konferencijaMapa.put("mjesto", mjesto.getNaziv());
        konferencijaMapa.put("pbr", String.valueOf(mjesto.getPbr()));
        rezultat.add(konferencijaMapa);
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis()))) {
            radService.plasman(konf.getNaziv());
            List<Map<String, String>> rezultati = kService.rezultati(pin);
            if(rezultati != null)rezultat.addAll(rezultati);
            return rezultat;
        }
        return rezultat;
    }

    @Override
    public Optional<Prisutan_na> findByPrisutanNaKljuc(PrisutanNaKljuc kljuc) {
        return prisRepo.findById(kljuc);
    }

    @Override
    public void glasaj(String korisnik, String naslov){
        Rad rad = radService.findByNaslovIgnoreCase(naslov);
        if(rad == null) Assert.hasText("","Rad with naslov " + naslov + " does not exists");

        Konferencija konf = rad.getKonferencija();
        Osoba osoba = oService.findByEmail(korisnik);
        PrisutanNaKljuc kljuc = new PrisutanNaKljuc();
        kljuc.setKorisnikId(osoba.getId());
        kljuc.setKonfId(konf.getId());
        if(prisRepo.findById(kljuc).isEmpty()) Assert.hasText("","You can't vote on this conference.");
        if(konf.getVrijemePocetka() == null || konf.getVrijemePocetka().after(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija hasn't started yet");
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija has already finished");
        Prisutan_na pris = prisRepo.findById(kljuc).get();
        if(pris.isGlasao())Assert.hasText("","You have already voted.");
        pris.setGlasao(true);
        rad.setUkupnoGlasova(rad.getUkupnoGlasova() + 1);
        prisRepo.save(pris);
    }
}
