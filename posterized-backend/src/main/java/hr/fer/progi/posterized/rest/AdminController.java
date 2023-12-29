package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
@RequestMapping("/stvoriAdmina")
public class AdminController {
    @Autowired
    private OsobaService oService;
    @PostMapping("")
    @Secured("superadmin")
    public void createAdminKorisnik(@RequestBody Osoba osoba) {
        String lozinka = osoba.getLozinka();
        oService.createAdminKorisnik(osoba);
        oService.saljiMail(osoba, lozinka);
    }
    /*@GetMapping("")
    public String createAdmin(){
        return "stvaranjeAdmina";
    }*/
}
