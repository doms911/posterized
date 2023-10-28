package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Korisnik;
import hr.fer.progi.posterized.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/korisnici")
public class KorisnikController {
    @Autowired
    private KorisnikService korisnikService;
    @GetMapping("")
    public List<Korisnik> listKorisnici() {
        return korisnikService.listAll();
    }
    @PostMapping("")
    public Korisnik createKorisnik(@RequestBody Korisnik korisnik) {
        return korisnikService.createKorisnik(korisnik);
    }
}
