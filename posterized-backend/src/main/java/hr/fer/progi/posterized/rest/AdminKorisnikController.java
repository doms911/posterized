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

@RestController
@RequestMapping("/registracija")
public class AdminKorisnikController {
    @Autowired
    private AdminKorisnikService akService;
    @GetMapping("")
    public String prikaziFormu() {
        return "Register";
    }
    @PostMapping("")
    public Osoba createAdminKorisnik(@RequestBody Osoba osoba) {
        return akService.createAdminKorisnik(osoba);
    }
}