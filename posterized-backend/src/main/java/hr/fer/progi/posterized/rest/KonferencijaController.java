package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Mjesto;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

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
        return kService.createKonferencija(Integer.valueOf(pin), email, naziv);
    }
    @PostMapping("/pin")
    public Konferencija provjeriPin(@RequestParam("pin") Integer pin){
        return kService.provjeriPin(pin);
    }
    @Secured("superadmin")
    @GetMapping("/prikaziSve")
    public List<Map<String, String>> prikazKonf() {
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
    public List<String> prikaz1(@AuthenticationPrincipal User user) {
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
    public List<Map<String, String>> prikaz2(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Konferencija> konferencije = kService.prikazAdmin(email);
        List<Map<String, String>> rezultat = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Konferencija konferencija : konferencije) {
            Map<String, String> konferencijaMapa = new HashMap<>();
            konferencijaMapa.put("naziv", konferencija.getNaziv());
            konferencijaMapa.put("urlVideo", konferencija.getUrlVideo());
            Timestamp vrijemePocetka = konferencija.getVrijemePocetka();
            Timestamp vrijemeKraja = konferencija.getVrijemeKraja();
            if (vrijemePocetka != null){
                konferencijaMapa.put("vrijemePocetka", dateFormat.format(vrijemePocetka));
            } else konferencijaMapa.put("vrijemePocetka", null);
            if (vrijemeKraja != null){
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
        }
        return rezultat;
    }
    /*@GetMapping("")
    public String prikaziKonf(){
        return "prikazKonferencije";
    }*/

    @Secured("admin")
    @PostMapping("/nadopuniKonf/{naziv}")
    public void updateKonferencija(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user,
                                           @RequestParam("urlVideo") String urlVideo,
                                           @RequestParam("vrijemePocetka") String vrijemePocetka,
                                           @RequestParam("vrijemeKraja") String vrijemeKraja,
                                           @RequestParam("mjesto") String mjesto, @RequestParam("pbr") String pbr,
                                           @RequestParam("sponzori") String sponzoriString){
        List<String> sponzori = Arrays.asList(sponzoriString.split(","));
        kService.updateKonferencija(user.getUsername(), nazivKonf, urlVideo, vrijemePocetka,vrijemeKraja, mjesto, pbr, sponzori);
    }

    @GetMapping("/izbrisiKonf/{naziv}")
    public void izbrisiKonferencija (@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user){
        kService.izbrisiKonf(user.getUsername(), nazivKonf);
    }

    @GetMapping("/dohvatiMjesto")
    public String dohvatiMjesto(@RequestParam("pin") String pin){
        return kService.dohvatiMjesto(Integer.valueOf(pin));
    };
}
