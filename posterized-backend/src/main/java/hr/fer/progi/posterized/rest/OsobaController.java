package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.OsobaService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Osoba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
@RequestMapping("/registracija")
public class OsobaController {
    @Autowired
    private OsobaService oService;
    @PostMapping("")
    public void createKorisnik(@RequestBody Osoba osoba) {
        oService.createAdminKorisnik(osoba);
    }

    @PostMapping("/admin")
    @Secured("superadmin")
    public void createAdmin(@RequestBody Osoba osoba) {
        String lozinka = osoba.getLozinka();
        oService.createAdminKorisnik(osoba);
        oService.saljiMail(osoba, lozinka);
    }
    /*@GetMapping("/admin")
    public String createAdmin(){
        return "stvaranjeAdmina";
    }*/
}