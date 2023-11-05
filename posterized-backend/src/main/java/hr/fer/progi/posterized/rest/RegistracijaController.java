package hr.fer.progi.posterized.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hr.fer.progi.posterized.domain.Korisnik;
import hr.fer.progi.posterized.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Controller
//@RestController
@RequestMapping("/registracija")
public class RegistracijaController {
    @Autowired
    private KorisnikService korisnikService;
    @GetMapping("")
    public String showRegistrationForm() {
        // Return the name of the HTML file without the extension
        return "registracija";
    }
    @PostMapping("")
    public Korisnik registrirajKorisnika(@RequestParam String email, @RequestParam String lozinka) {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(email);
        korisnik.setLozinka(lozinka);
        return korisnikService.createKorisnik(korisnik);
    }
}