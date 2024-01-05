package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
//@Controller
@RequestMapping("/konferencija")

public class KonferencijaController {
    @Autowired
    private KonferencijaService kService;
    @Secured("superadmin")
    @PostMapping("/stvoriKonf")
    public Konferencija createKonferencija(@RequestParam("pin") String pin, @RequestParam("adminEmail") String email,
                                           @RequestParam("naziv") String naziv){
        return kService.createKonferencija(pin, email, naziv);
    }

    @Secured("superadmin")
    @GetMapping("/prikaziSve")
    public List<Map<String, String>> prikazSvihKonf() {
        List<Konferencija> konferencije = kService.listAll();
        List<Map<String, String>> rezultat = new ArrayList<>();

        for (Konferencija konferencija : konferencije) {
            Map<String, String> konferencijaMapa = new HashMap<>();
            konferencijaMapa.put("naziv", konferencija.getNaziv());
            konferencijaMapa.put("pin", String.valueOf(konferencija.getPin()));

            Osoba admin = konferencija.getAdminKonf();
            konferencijaMapa.put("adminIme", admin.getIme());
            konferencijaMapa.put("adminPrezime", admin.getPrezime());
            konferencijaMapa.put("adminEmail", admin.getEmail());

            rezultat.add(konferencijaMapa);
        }
        return rezultat;
    }
    @Secured("admin")
    @GetMapping("/prikaziAdminuNazive")
    public List<String> prikazAdminuNazive(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Konferencija> konferencije = kService.prikazAdmin(email);
        List<String> rezultat = new ArrayList<>();

        for (Konferencija konferencija : konferencije) {
            rezultat.add(konferencija.getNaziv());
        }
        return rezultat;
    }
    @Secured("admin")
    @GetMapping("/prikaziAdminuKonf/{naziv}")
    public List<Map<String, String>> prikazAdminuKonf(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Konferencija> konferencije = kService.prikazAdmin(email);
        List<Map<String, String>> rezultat = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Konferencija konferencija : konferencije) {
            if(konferencija.getNaziv().equalsIgnoreCase(nazivKonf)) {
                Map<String, String> konferencijaMapa = new HashMap<>();
                konferencijaMapa.put("naziv", konferencija.getNaziv());
                konferencijaMapa.put("pin", String.valueOf(konferencija.getPin()));
                konferencijaMapa.put("urlVideo", konferencija.getUrlVideo());
                konferencijaMapa.put("adresa", konferencija.getAdresa());
                Timestamp vrijemePocetka = konferencija.getVrijemePocetka();
                Timestamp vrijemeKraja = konferencija.getVrijemeKraja();
                if (vrijemePocetka != null) {
                    konferencijaMapa.put("vrijemePocetka", dateFormat.format(vrijemePocetka));
                } else konferencijaMapa.put("vrijemePocetka", null);
                if (vrijemeKraja != null) {
                    konferencijaMapa.put("vrijemeKraja", dateFormat.format(vrijemeKraja));
                } else konferencijaMapa.put("vrijemeKraja", null);

                Mjesto mjesto = konferencija.getMjesto();
                if (mjesto != null) {
                    konferencijaMapa.put("mjesto", mjesto.getNaziv());
                    konferencijaMapa.put("pbr", String.valueOf(mjesto.getPbr()));
                } else {
                    konferencijaMapa.put("mjesto", null);
                    konferencijaMapa.put("pbr", null);
                }
                rezultat.add(konferencijaMapa);

                for(Rad rad : konferencija.getRadovi()){
                    Map<String, String> radMapa = new HashMap<>();
                    radMapa.put("naslov", rad.getNaslov());
                    radMapa.put("ukupnoGlasova", String.valueOf(rad.getUkupnoGlasova()));
                    radMapa.put("urlPptx", rad.getUrlPptx());
                    radMapa.put("urlPoster", rad.getUrlPoster());
                    Osoba autor = rad.getAutor();
                    radMapa.put("ime", autor.getIme());
                    radMapa.put("prezime", autor.getPrezime());
                    radMapa.put("mail", autor.getEmail());
                    rezultat.add(radMapa);
                }
                return rezultat;
            }
        }
        return rezultat;
    }
    /*@GetMapping("")
    public String prikaziKonf(){
        return "prikazKonferencije";
    }*/

    @Secured("admin")
    @PostMapping("/nadopuniKonf/{naziv}")
    public void updateKonf(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user,
                                           @RequestParam("urlVideo") String urlVideo,
                                           @RequestParam("vrijemePocetka") String vrijemePocetka,
                                           @RequestParam("vrijemeKraja") String vrijemeKraja,
                                           @RequestParam("mjesto") String mjesto, @RequestParam("pbr") String pbr,
                                            @RequestParam("adresa") String adresa,
                                   @RequestParam("sponzori") List<String> sponzori){
        kService.updateKonferencija(user.getUsername(), nazivKonf, urlVideo, vrijemePocetka,vrijemeKraja, mjesto, pbr, adresa, sponzori);
    }

    @GetMapping("/izbrisiKonf/{naziv}")
    public void izbrisiKonf (@PathVariable("naziv") String nazivKonf){
        kService.izbrisiKonf(nazivKonf);
    }

    @GetMapping("/dohvatiMjesto/{pin}")
    public String dohvatiMjesto(@PathVariable("pin") String pin){
        return kService.dohvatiMjesto(Integer.valueOf(pin));
    };
    @GetMapping("/dohvatiVideo/{pin}")
    public String dohvatiVideo(@PathVariable("pin") String pin){
        return kService.dohvatiVideo(Integer.valueOf(pin));
    };

    @Secured("admin")
    @PostMapping("/zavrsiKonf/{naziv}")
    public void zavrsiKonf(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user,
                           @RequestParam("vrijeme") String vrijeme,
                           @RequestParam("lokacija") String lokacija) {
        String email = user.getUsername();
        kService.zavrsiKonferencija(email, nazivKonf);
        kService.saljiMail(nazivKonf, vrijeme, lokacija);
    }

    @GetMapping("/dohvatiRadove/{pin}")
    public List<Map<String, String>> prikazRadova(@PathVariable("pin") String pin) {
        if (kService.countByPin(Integer.valueOf(pin)) == 0){
            Assert.hasText("","Konferencija ne postoji.");
        }
        Konferencija konf = kService.findByPin(Integer.valueOf(pin));
        if(konf.getVrijemePocetka()==null)Assert.hasText("","Konferencija još nije počela.");
        List<Map<String, String>> rezultat = new ArrayList<>();
        for(Rad rad : konf.getRadovi()){
            Map<String, String> radMapa = new HashMap<>();
            radMapa.put("naslov", rad.getNaslov());
            radMapa.put("urlPptx", rad.getUrlPptx());
            radMapa.put("urlPoster", rad.getUrlPoster());
            //radMapa.put("ukupnoGlasova", String.valueOf(rad.getUkupnoGlasova()));
            rezultat.add(radMapa);
        }
        return rezultat;
    }

    @GetMapping("/pokrovitelji/{pin}")
    public List<Map<String, String>> dohvatiPokrovitelje(@PathVariable("pin") String pin){
        if (kService.countByPin(Integer.valueOf(pin)) == 0){
            Assert.hasText("","Konferencija ne postoji.");
        }
        Konferencija konf = kService.findByPin(Integer.valueOf(pin));
        List<Map<String, String>> rezultat = new ArrayList<>();
        for(Pokrovitelj pokr : konf.getPokrovitelji()){
            Map<String, String> pokrMapa = new HashMap<>();
            pokrMapa.put("naziv", pokr.getNaziv());
            pokrMapa.put("url", pokr.getUrl());
            pokrMapa.put("urlSlike", pokr.getUrlSlike());
            rezultat.add(pokrMapa);
        }
        return rezultat;
    }

    @Secured("admin")
    @GetMapping("/prikaziAdminuSponzoreKonf/{naziv}")
    public List<String> prikaziAdminuSponzoreKonf(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Konferencija> konferencije = kService.prikazAdmin(email);
        List<String> rezultat = new ArrayList<>();

        for (Konferencija konferencija : konferencije) {
            if(konferencija.getNaziv().equalsIgnoreCase(nazivKonf)) {
                for (Pokrovitelj pokr : konferencija.getPokrovitelji()){
                    rezultat.add(pokr.getNaziv());
                }
                return rezultat;
            }
        }
        return rezultat;
    }
}
