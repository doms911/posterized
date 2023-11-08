package hr.fer.progi.posterized.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Controller
//@RestController
@RequestMapping("/registracija")
public class RegistracijaController {
    @Autowired
    private OsobaService osobaService;
    @GetMapping("")
    public String showRegistrationForm() {
        // Return the name of the HTML file without the extension
        return "registracija";
    }
    @PostMapping("")
    public Osoba registrirajOsobu(@RequestParam String email, @RequestParam String lozinka, @RequestParam String ime,
                                  @RequestParam String prezime) {
        Osoba osoba = new Osoba();
        osoba.setEmail(email);
        osoba.setLozinka(lozinka);
        osoba.setIme(ime);
        osoba.setPrezime(prezime);
        osoba.setUloga("korisnik");
        return osobaService.createOsoba(osoba);
    }
}