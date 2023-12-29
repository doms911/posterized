package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.KonferencijaRepository;
import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.*;
import jakarta.transaction.Transactional;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


@Service
public class KonferencijaServiceJPA implements KonferencijaService {
    @Autowired
    private KonferencijaRepository konferencijaRepo;
    @Autowired
    private AdminKorisnikService akService;
    @Autowired
    private MjestoService mjService;
    @Autowired
    private PokroviteljService pokrService;
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";

    @Override
    public List<Konferencija> listAll(){
        return konferencijaRepo.findAll();
    }

    @Override
    public Konferencija findByNazivIgnoreCase(String naziv){
        return konferencijaRepo.findByNazivIgnoreCase(naziv);
    }

    @Override
    public List<Konferencija> prikazAdmin(String email){
        Osoba osoba = akService.findByEmail(email);
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
    public String dohvatiMjesto(Integer pin){
        if (konferencijaRepo.countByPin(pin) == 0){
            Assert.hasText("","Konferencija does not exist.");
        }
        Konferencija konf = konferencijaRepo.findByPin(pin);
        if (konf.getMjesto() == null){
            Assert.hasText("","Place isn't set.");
        }

        String apiKey="6THFAJBEM3ED86RX799RZ3YTJ";
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder vrijeme = new StringBuilder(dtf1.format(now));
        vrijeme.append("T");
        vrijeme.append(dtf2.format(now));

        String apiEndPoint="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

        StringBuilder requestBuilder=new StringBuilder(apiEndPoint);
        requestBuilder.append(konf.getMjesto().getNaziv());
        requestBuilder.append(",HR/");
        requestBuilder.append(vrijeme);

        URIBuilder builder = null;
        try {
            builder = new URIBuilder(requestBuilder.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        builder.setParameter("key", apiKey);

        return builder.toString();
    }

    @Override
    public Konferencija createKonferencija(Integer pin, String email, String naziv) {
        Assert.notNull(pin, "Pin must be given.");
        if (konferencijaRepo.countByPin(pin) > 0){
            Assert.hasText("","Konferencija already exists.");
        }
        Konferencija konferencija = new Konferencija();
        Assert.hasText(naziv, "Naziv must be given");
        if (konferencijaRepo.countByNazivCaseInsensitive(naziv) > 0){
            Assert.hasText("","Naziv already exists.");
        }
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email must be in a valid format, e.g., user@example.com, not '" + email + "'"
        );
        if (akService.countByEmail(email) == 0) {
            Assert.hasText("", "Osoba with email " + email + " does not exists");
        }
        konferencija.setPin(pin);
        konferencija.setNaziv(naziv);
        konferencija.setAdminKonf(akService.findByEmail(email));
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
    public void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjestoNaziv, String pbr, List<String> sponzori) {
        Konferencija novaKonferencija = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(novaKonferencija == null) Assert.hasText("","Konferencija with naziv " + naziv + " does not exists");
        if(!novaKonferencija.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");
        Timestamp vrijemePocetkaT = null, vrijemeKrajaT = null;
        if(!vrijemePocetka.isEmpty()) {
            vrijemePocetkaT = Timestamp.valueOf(vrijemePocetka.replace("T", " ") + ":00");
        } else {
            if(novaKonferencija.getVrijemePocetka() == null)
            Assert.hasText("", "VrijemePocetka must be given");
        }
        if(!vrijemeKraja.isEmpty()) {
            vrijemeKrajaT = Timestamp.valueOf(vrijemeKraja.replace("T", " ") + ":00");
        } else {
            if(novaKonferencija.getVrijemeKraja() == null)
                Assert.hasText("", "VrijemeKraja must be given");
        }
        if (!vrijemePocetka.isEmpty() && !vrijemeKraja.isEmpty() && vrijemePocetkaT.after(vrijemeKrajaT)) {
            Assert.hasText("","The end of the conference must be after the start of the conference.");
        } else if (vrijemePocetka.isEmpty() && !vrijemeKraja.isEmpty() && novaKonferencija.getVrijemePocetka().after(vrijemeKrajaT)){
            Assert.hasText("","The end of the conference must be after the start of the conference.");
        } else if (!vrijemePocetka.isEmpty() && vrijemeKraja.isEmpty() && vrijemePocetkaT.after(novaKonferencija.getVrijemeKraja())){
            Assert.hasText("","The end of the conference must be after the start of the conference.");
        }

        if(urlVideo.isEmpty() && novaKonferencija.getUrlVideo() == null) Assert.hasText("", "UrlVideo must be given");

        if((pbr.isEmpty() || mjestoNaziv.isEmpty()) && novaKonferencija.getMjesto() == null ) Assert.hasText("", "Mjesto must be given");

        if((!pbr.isEmpty() && mjestoNaziv.isEmpty()) || (pbr.isEmpty() && !mjestoNaziv.isEmpty()))
            Assert.hasText("", "Pbr and mjesto must be changed together");

        Mjesto mjesto=null;
        if(!pbr.isEmpty() ) mjesto = mjService.findByPbr(Integer.valueOf(pbr));
        if(mjesto != null) {
            mjService.update(mjestoNaziv, Integer.valueOf(pbr));
            novaKonferencija.setMjesto(mjesto);
        } else{
            novaKonferencija.setMjesto(mjService.createMjesto(Integer.valueOf(pbr), mjestoNaziv));
        }

        if(!sponzori.isEmpty() && !(sponzori.size() == 1 && sponzori.get(0).isEmpty())){
            for (Pokrovitelj pokrovitelj : novaKonferencija.getPokrovitelji()){
                pokrovitelj.getKonferencije().remove(novaKonferencija);
            }
            novaKonferencija.getPokrovitelji().clear();
            for (String sponzor : sponzori){
                Pokrovitelj pokr = pokrService.findByNazivIgnoreCase(sponzor);
                if(pokr != null){
                    pokr.getKonferencije().add(novaKonferencija);
                    novaKonferencija.getPokrovitelji().add(pokr);
                }
            }
        }
        if(!urlVideo.isEmpty()){novaKonferencija.setUrlVideo(urlVideo);}
        if(!vrijemeKraja.isEmpty())novaKonferencija.setVrijemeKraja(vrijemeKrajaT);
        if(!vrijemePocetka.isEmpty())novaKonferencija.setVrijemePocetka(vrijemePocetkaT);
        konferencijaRepo.save(novaKonferencija);
    }
    @Override
    @Transactional
    public void izbrisiKonf(String admin, String naziv){
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + naziv + " does not exists");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");
        for (Pokrovitelj pokr : pokrService.listAll()){
            pokr.getKonferencije().remove(konf);
        }
        konf.getFotografije().clear();
        konferencijaRepo.deleteByNazivIgnoreCase(naziv);
    }
}
