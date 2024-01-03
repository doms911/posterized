package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.KonferencijaRepository;
import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.*;
import jakarta.transaction.Transactional;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


@Service
public class KonferencijaServiceJPA implements KonferencijaService {
    @Autowired
    private KonferencijaRepository konferencijaRepo;
    @Autowired
    private OsobaService oService;
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
    public int countByPin(Integer pin){
        return konferencijaRepo.countByPin(pin);
    }
    @Override
    public Konferencija findByPin(Integer pin){
        return konferencijaRepo.findByPin(pin);
    }
    @Override
    public Konferencija findByNazivIgnoreCase(String naziv){
        return konferencijaRepo.findByNazivIgnoreCase(naziv);
    }

    @Override
    public List<Konferencija> prikazAdmin(String email){
        Osoba osoba = oService.findByEmail(email);
        return konferencijaRepo.findAllByAdminKonf_id(osoba.getId());
    };

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
        if (oService.countByEmail(email) == 0) {
            Assert.hasText("", "Osoba with email " + email + " does not exists");
        }
        konferencija.setPin(pin);
        konferencija.setNaziv(naziv);
        konferencija.setAdminKonf(oService.findByEmail(email));
        return konferencijaRepo.save(konferencija);
    }


    @Override
    public void zavrsiKonferencija(String admin, String naziv) {
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + naziv + " does not exists");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija hasn't started yet");
        long currentTimeWithoutMillis = System.currentTimeMillis() / 1000 * 1000;
        Timestamp timestampWithoutMillis = new Timestamp(currentTimeWithoutMillis);
        konf.setVrijemeKraja(timestampWithoutMillis);
        konferencijaRepo.save(konf);
    }
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void saljiMail(String naziv){
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Instant endTime = konf.getVrijemeKraja().toInstant().plus(5, ChronoUnit.DAYS);
        Date endDate = Date.from(endTime);

        List<Map<String, String>> rezultati = rezultati(konf.getPin());
        if(rezultati == null) return;
        String poruka = "You are invited to the award ceremony for the conference " +
                konf.getNaziv() + " at " + konf.getAdresa() + ", " + konf.getMjesto().getNaziv() +
                ", " + konf.getMjesto().getPbr() + ". The ceremony will take place on " +
                dateFormat.format(endDate);

        StringBuilder message = new StringBuilder(poruka);
        message.append(". The awards have been won by the following winners:");

        for (int i = 0; i < rezultati.size(); i++) {
            Map<String, String> winner = rezultati.get(i);
            String name = winner.get("naslov");
            String glasovi = winner.get("ukupnoGlasova");
            String plasman = winner.get("plasman");
            if(Integer.valueOf(plasman) >=4)break;
            message.append("\n- ").append(plasman).append(". place : ").append(name).append(", ").append(glasovi).append(" votes");
        }
        message.append(".\n\n");
        Set<Rad> radovi = konf.getRadovi();
        for (int i = 0; i < rezultati.size(); i++) {
            StringBuilder messageBuilder = new StringBuilder();
            Map<String, String> winner = rezultati.get(i);
            String name = winner.get("naslov");
            String glasovi = winner.get("ukupnoGlasova");
            String plasman = winner.get("plasman");
            messageBuilder.append("Your Rad '").append(name).append("' has won ").append(glasovi)
                    .append(" votes and secured ").append(plasman).append(". place.\n");

            Rad rad = radovi.stream()
                    .filter(rad2 -> rad2.getNaslov().equals(name))
                    .findFirst()
                    .orElse(null);
            final SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(rad.getAutor().getEmail());
            email.setSubject("Invitation to award ceremony and results");
            email.setText(message.toString() + messageBuilder);
            email.setFrom(env.getProperty("support.email"));
            mailSender.send(email);
        }

        for(Prisutan_na pris : konf.getPrisutnost()) {
            String korisnik = pris.getKorisnik().getEmail();
            if(radovi.stream().anyMatch(rad2 -> rad2.getAutor().getEmail().equals(korisnik)))continue;
            final SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(korisnik);
            email.setSubject("Invitation to award ceremony");
            email.setText(message.toString());
            email.setFrom(env.getProperty("support.email"));
            mailSender.send(email);
        }
    }
    @Override
    public void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjestoNaziv, String pbr, String adresa, List<String> sponzori) {
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

        if(adresa.isEmpty() && novaKonferencija.getAdresa() == null) Assert.hasText("", "Adresa must be given");

        if((pbr.isEmpty() || mjestoNaziv.isEmpty()) && novaKonferencija.getMjesto() == null ) Assert.hasText("", "Mjesto must be given");

        if((!pbr.isEmpty() && mjestoNaziv.isEmpty()) || (pbr.isEmpty() && !mjestoNaziv.isEmpty()))
            Assert.hasText("", "Pbr and mjesto must be changed together");

        Mjesto mjesto;
        if(!pbr.isEmpty() ) {
            mjesto = mjService.findByPbr(Integer.valueOf(pbr));

            if (mjesto != null) {
                mjService.update(mjestoNaziv, Integer.valueOf(pbr));
                novaKonferencija.setMjesto(mjesto);
            } else {
                novaKonferencija.setMjesto(mjService.createMjesto(Integer.valueOf(pbr), mjestoNaziv));
            }
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
        } else if (sponzori.isEmpty())novaKonferencija.getPokrovitelji().clear();

        if(!urlVideo.isEmpty()){novaKonferencija.setUrlVideo(urlVideo);}
        if(!adresa.isEmpty()){novaKonferencija.setAdresa(adresa);}
        if(!vrijemeKraja.isEmpty())novaKonferencija.setVrijemeKraja(vrijemeKrajaT);
        if(!vrijemePocetka.isEmpty())novaKonferencija.setVrijemePocetka(vrijemePocetkaT);
        konferencijaRepo.save(novaKonferencija);
    }
    @Override
    @Transactional
    public void izbrisiKonf(String naziv){
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + naziv + " does not exists");
        if(konf.getVrijemePocetka() != null && konf.getVrijemePocetka().after(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija has already started");
        for (Pokrovitelj pokr : pokrService.listAll()){
            pokr.getKonferencije().remove(konf);
        }
        konf.getFotografije().clear();
        konf.getRadovi().clear();
        konf.getPrisutnost().clear();

        Media objekt = new Media();
        objekt.deleteFolder(naziv);
        konferencijaRepo.deleteByNazivIgnoreCase(naziv);
    }

    @Override
    public List<Map<String, String>> rezultati(Integer pin){
        Konferencija konf = konferencijaRepo.findByPin(pin);
        if(konf == null) Assert.hasText("","Konferencija with pin " + pin + " does not exists");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija hasn't started yet");
        Set<Rad> radovi = konf.getRadovi();
        List<Rad> radoviList = new ArrayList<>(radovi);
        for (Rad rad : radoviList) {
            if (rad.getPlasman() == null) {
                return null;
            }
        }
        radoviList.sort(Comparator.comparing(Rad::getPlasman));

        List<Map<String, String>> rez = new ArrayList<>();

        for(Rad rad : radoviList){
            Map<String, String> mapa = new HashMap<>();
            mapa.put("naslov", rad.getNaslov());
            mapa.put("urlPoster", rad.getUrlPoster());
            mapa.put("urlPptx", rad.getUrlPptx());
            mapa.put("ukupnoGlasova", String.valueOf(rad.getUkupnoGlasova()));
            mapa.put("plasman", String.valueOf(rad.getPlasman()));
            rez.add(mapa);
        }
        return rez;
    }

    @Override
    public String dohvatiVideo(Integer pin) {
        Konferencija konf = konferencijaRepo.findByPin(pin);
        if(konf == null) Assert.hasText("","Konferencija with pin " + pin + " does not exists");
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija has already finished");
        return konf.getUrlVideo();
    }
}
