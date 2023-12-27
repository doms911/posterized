package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
//@Controller
@RequestMapping("/konferencija")

public class KonferencijaController {
    @Autowired
    private KonferencijaService kService;

    @PostMapping("/stvoriKonf")
    public Konferencija createKonferencija(@RequestParam("pin") String pin, @RequestParam("adminEmail") String email,
                                           @RequestParam("naziv") String naziv){
        return kService.createKonferencija(Integer.valueOf(pin), email, naziv);
    }
    @PostMapping("/pin")
    public Konferencija provjeriPin(@RequestParam("pin") Integer pin){
        return kService.provjeriPin(pin);
    }

    @GetMapping("/prikaziSve")
    public List<Map<String, String>> prikazKonf() {
        List<Konferencija> konferencije = kService.listAll();
        List<Map<String, String>> rezultat = new ArrayList<>();

        for (Konferencija konferencija : konferencije) {
            Map<String, String> konferencijaMapa = new HashMap<>();
            konferencijaMapa.put("naziv", konferencija.getNaziv());
            konferencijaMapa.put("pin", konferencija.getPin().toString());

            Osoba admin = konferencija.getAdminKonf();
            konferencijaMapa.put("adminIme", admin.getIme());
            konferencijaMapa.put("adminPrezime", admin.getPrezime());
            konferencijaMapa.put("adminEmail", admin.getEmail());

            rezultat.add(konferencijaMapa);
        }

        return rezultat;
    }

    /*@GetMapping("")
    public String prikaziKonf(){
        return "prikazKonferencije";
    }*/

    @PostMapping("/nadopuniKonf")
    public Konferencija updateKonferencija(@RequestParam("pin") Integer pin){
        return kService.provjeriPin(pin);
    }
}
