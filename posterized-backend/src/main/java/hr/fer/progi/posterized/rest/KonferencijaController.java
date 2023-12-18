package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/konferencija")
public class KonferencijaController {
    @Autowired
    private KonferencijaService kService;

    @PostMapping("")
    public Konferencija createKonferencija(@RequestBody Integer pin){
        return kService.createKonferencija(pin);
    }
}
