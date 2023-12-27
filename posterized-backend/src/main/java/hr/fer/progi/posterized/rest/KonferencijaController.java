package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import hr.fer.progi.posterized.domain.Konferencija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*@RestController*/
@Controller
@RequestMapping("")
public class KonferencijaController {
    @Autowired
    private KonferencijaService kService;

    @PostMapping("/addConference")
    public Konferencija createKonferencija(@RequestParam("pin") String pin, @RequestParam("adminEmail") String email){
        return kService.createKonferencija(Integer.valueOf(pin), email);
    }
    @PostMapping("/pin")
    public Konferencija provjeriPin(@RequestParam("pin") Integer pin){
        return kService.provjeriPin(pin);
    }

    @GetMapping("/konferencije")
    public List<Konferencija> dohvatiKonferencije(){
        return kService.listAll();
    }
    @GetMapping("")
    public String prikazKonf(){
        return "prikazKonferencije";
    }
}
