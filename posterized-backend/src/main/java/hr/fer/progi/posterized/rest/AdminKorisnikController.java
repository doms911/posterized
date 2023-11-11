package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.AdminKorisnikService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Osoba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RestController
@RequestMapping("/registracija")
public class AdminKorisnikController {
    @Autowired
    private AdminKorisnikService akService;
    @GetMapping("")
    public String prikaziFormu() {
        return "registracija";
    }
    @PostMapping("")
    public Osoba createAdminKorisnik(@RequestParam String email, @RequestParam String lozinka, @RequestParam String ime,
                                  @RequestParam String prezime, @RequestParam String uloga) {
        Osoba osoba = new Osoba();
        osoba.setEmail(email);
        osoba.setLozinka(lozinka);
        osoba.setIme(ime);
        osoba.setPrezime(prezime);
        osoba.setUloga(uloga);
        return akService.createAdminKorisnik(osoba);
    }
}