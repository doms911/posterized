package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.AdminKorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
@RequestMapping("/stvoriAdmina")
public class AdminController {
    @Autowired
    private AdminKorisnikService akService;
    @PostMapping("")
    public void createAdminKorisnik(@RequestBody Osoba osoba) {
        String lozinka = osoba.getLozinka();
        akService.createAdminKorisnik(osoba);
        akService.saljiMail(osoba, lozinka);
    }
    /*@GetMapping("")
    public String createAdmin(){
        return "stvaranjeAdmina";
    }*/
}
