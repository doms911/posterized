package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/addConference")
public class KonferencijaController {
    @Autowired
    private KonferencijaService kService;

    @PostMapping("")
    public Konferencija createKonferencija(@RequestParam("pin") String pin){
        return kService.createKonferencija(Integer.valueOf(pin));
    }
}
