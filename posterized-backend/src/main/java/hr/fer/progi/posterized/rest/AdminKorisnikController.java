package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.AdminKorisnikService;
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
    @PostMapping("")
    public Osoba createAdminKorisnik(@RequestBody Osoba osoba) {
        System.out.println("doslo");
        return akService.createAdminKorisnik(osoba);
    }
}