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
            Assert.hasText("","Konferencija ne postoji.");
        }
        Konferencija konf = konferencijaRepo.findByPin(pin);
        if (konf.getMjesto() == null){
            Assert.hasText("","Mjesto ne postoji.");
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
        StringBuilder builderNew = new StringBuilder(builder.toString());
        builderNew.append("&include=current");
        builderNew.append("&unitGroup=metric");

        return builderNew.toString();
    }

    @Override
    public void createKonferencija(String pinS, String email, String naziv) {
        Assert.hasText(pinS, "Pin mora biti naveden.");
        if (!pinS.matches("\\d+")) {
            Assert.hasText("","Pin mora sadržavati samo brojeve.");
        }
        Integer pin = Integer.valueOf(pinS);
        if (konferencijaRepo.countByPin(pin) > 0){
            Assert.hasText("","Konferencija već postoji.");
        }
        Konferencija konferencija = new Konferencija();
        Assert.hasText(naziv, "Naziv mora biti naveden.");
        if (konferencijaRepo.countByNazivCaseInsensitive(naziv) > 0){
            Assert.hasText("","Konferencija s istim nazivom već postoji.");
        }
        Assert.hasText(email, "Email mora biti naveden.");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email mora biti u ispravnom obliku, npr. user@example.com, a ne '" + email + "'."
        );
        Osoba osoba = oService.findByEmail(email);
        if (oService.countByEmail(email) == 0 || osoba.getUloga().equals("autor")) {
            Assert.hasText("", "Admin s emailom " + email + " ne postoji.");
        }
        if(osoba.getUloga().equals("korisnik")) {
            Osoba o1 = new Osoba();
            o1.setUloga("admin");
            o1.setId(null);
            o1.setEmail(osoba.getEmail());
            o1.setIme(osoba.getIme());
            o1.setPrezime(osoba.getPrezime());
            o1.setLozinka(osoba.getLozinka());
            oService.createAdminKorisnik(o1);
        }
        konferencija.setPin(pin);
        konferencija.setNaziv(naziv);
        konferencija.setAdminKonf(osoba);
        konferencijaRepo.save(konferencija);
    }


    @Override
    public void zavrsiKonferencija(String admin, String naziv) {
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(konf == null) Assert.hasText("","Konferencija s nazivom " + naziv + " ne postoji.");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija još nije počela.");
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
    public void saljiMail(String naziv, String vrijeme, String lokacija){
        Assert.hasText(vrijeme, "Vrijeme dodjele nagrade mora biti navedeno.");
        Assert.hasText(lokacija, "Lokacija dodjele nagrade mora biti navedena.");
        Konferencija konf = konferencijaRepo.findByNazivIgnoreCase(naziv);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<Map<String, String>> rezultati = rezultati(konf.getPin());
        if(rezultati == null) return;
        String poruka = "Pozvani ste na dodjelu nagrada za konferenciju pod nazivom " +
                konf.getNaziv() + " na adresi " + lokacija + ". Dodjela nagrada održat će se " +
                vrijeme;

        StringBuilder message = new StringBuilder(poruka);
        message.append(". Nagrade su osvojili sljedeći natjecatelji:");

        for (int i = 0; i < rezultati.size(); i++) {
            Map<String, String> winner = rezultati.get(i);
            String name = winner.get("naslov");
            String glasovi = winner.get("ukupnoGlasova");
            String plasman = winner.get("plasman");
            if(Integer.valueOf(plasman) >=4)break;
            message.append("\n- ").append(plasman).append(". mjesto : ").append(name).append(", ").append(glasovi).append(" glasova");
        }
        message.append(".\n\n");
        Set<Rad> radovi = konf.getRadovi();
        for (int i = 0; i < rezultati.size(); i++) {
            StringBuilder messageBuilder = new StringBuilder();
            Map<String, String> winner = rezultati.get(i);
            String name = winner.get("naslov");
            String glasovi = winner.get("ukupnoGlasova");
            String plasman = winner.get("plasman");
            messageBuilder.append("Vaš rad '").append(name).append("' je osvojio ").append(glasovi)
                    .append(" glasova i osvojio je ").append(plasman).append(". mjesto!\n");

            Rad rad = radovi.stream()
                    .filter(rad2 -> rad2.getNaslov().equals(name))
                    .findFirst()
                    .orElse(null);
            final SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(rad.getAutor().getEmail());
            email.setSubject("Pozivnica na dodjelu nagrada i rezultati");
            email.setText(message.toString() + messageBuilder);
            email.setFrom(env.getProperty("support.email"));
            mailSender.send(email);
        }

        for(Prisutan_na pris : konf.getPrisutnost()) {
            String korisnik = pris.getKorisnik().getEmail();
            if(radovi.stream().anyMatch(rad2 -> rad2.getAutor().getEmail().equals(korisnik)))continue;
            final SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(korisnik);
            email.setSubject("Pozivnica na dodjelu nagrada");
            email.setText(message.toString());
            email.setFrom(env.getProperty("support.email"));
            mailSender.send(email);
        }
    }
    @Override
    public void updateKonferencija(String admin, String naziv, String urlVideo, String vrijemePocetka, String vrijemeKraja, String mjestoNaziv, String pbr, String adresa, List<String> sponzori) {
        Konferencija novaKonferencija = konferencijaRepo.findByNazivIgnoreCase(naziv);
        if(novaKonferencija == null) Assert.hasText("","Konferencija s nazivom " + naziv + " ne postoji.");
        if(!novaKonferencija.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","Nemate pristup ovoj konferenciji.");
        Timestamp vrijemePocetkaT = null, vrijemeKrajaT = null;
        if(!vrijemePocetka.isEmpty()) {
            vrijemePocetkaT = Timestamp.valueOf(vrijemePocetka + ":00");
        } else {
            if(novaKonferencija.getVrijemePocetka() == null)
            Assert.hasText("", "Vrijeme početka konferencije mora biti navedeno.");
        }
        if(!vrijemeKraja.isEmpty()) {
            vrijemeKrajaT = Timestamp.valueOf(vrijemeKraja + ":00");
        } else {
            if(novaKonferencija.getVrijemeKraja() == null)
                Assert.hasText("", "Vrijeme kraja konferencije mora biti navedeno.");
        }
        if (!vrijemePocetka.isEmpty() && !vrijemeKraja.isEmpty() && vrijemePocetkaT.after(vrijemeKrajaT)) {
            Assert.hasText("","Kraj konferencije mora biti nakon početka iste.");
        } else if (vrijemePocetka.isEmpty() && !vrijemeKraja.isEmpty() && novaKonferencija.getVrijemePocetka().after(vrijemeKrajaT)){
            Assert.hasText("","Kraj konferencije mora biti nakon početka iste.");
        } else if (!vrijemePocetka.isEmpty() && vrijemeKraja.isEmpty() && vrijemePocetkaT.after(novaKonferencija.getVrijemeKraja())){
            Assert.hasText("","Kraj konferencije mora biti nakon početka iste.");
        }

        if(urlVideo.isEmpty() && novaKonferencija.getUrlVideo() == null) Assert.hasText("", "URL video prijenosa mora biti naveden.");

        if(adresa.isEmpty() && novaKonferencija.getAdresa() == null) Assert.hasText("", "Adresa mora biti navedena");

        if((pbr.isEmpty() || mjestoNaziv.isEmpty()) && novaKonferencija.getMjesto() == null ) Assert.hasText("", "Mjesto mora biti navedeno.");

        if((!pbr.isEmpty() && mjestoNaziv.isEmpty()) || (pbr.isEmpty() && !mjestoNaziv.isEmpty()))
            Assert.hasText("", "Poštanski broj i mjesto moraju biti promijenjeni zajedno.");

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
        }

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
        if(konf == null) Assert.hasText("","Konferencija s nazivom " + naziv + " ne postoji.");
        if(konf.getVrijemePocetka() != null && konf.getVrijemePocetka().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija je već počela.");
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
        if(konf == null) Assert.hasText("","Konferencija s pinom " + pin + " ne postoji.");
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija još nije počela.");
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
            mapa.put("ime", rad.getAutor().getIme());
            mapa.put("prezime", rad.getAutor().getPrezime());
            rez.add(mapa);
        }
        return rez;
    }

    @Override
    public String dohvatiVideo(Integer pin) {
        Konferencija konf = konferencijaRepo.findByPin(pin);
        if(konf == null) Assert.hasText("","Konferencija s pinom " + pin + " ne postoji.");
        if(konf.getVrijemeKraja() != null && konf.getVrijemeKraja().before(new Timestamp(System.currentTimeMillis())))
            Assert.hasText("","Konferencija je već završila.");
        return konf.getUrlVideo();
    }
}
