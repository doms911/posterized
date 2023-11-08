package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/osobe")
public class OsobaController {
    @Autowired
    private OsobaService osobaService;
    @GetMapping("")
    public List<Osoba> listOsobe() {
        return osobaService.listAll();
    }
    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public Osoba createOsoba(@RequestBody Osoba osoba) {
        return osobaService.createOsoba(osoba);
    }
}
