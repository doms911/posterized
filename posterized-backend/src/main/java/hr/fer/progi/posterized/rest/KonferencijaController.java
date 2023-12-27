package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
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

    @GetMapping("")
    public String prikazKonf() {
        return "prikazKonferencije";
    }

    @PostMapping("/nadopuniKonf")
    public Konferencija updateKonferencija(@RequestParam("pin") Integer pin){
        return kService.provjeriPin(pin);

    }
}
