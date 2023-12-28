package hr.fer.progi.posterized.rest;


import hr.fer.progi.posterized.domain.Pokrovitelj;
import hr.fer.progi.posterized.service.PokroviteljService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@Controller
@RequestMapping("/pokrovitelj")
public class PokroviteljController {
    @Autowired
    private PokroviteljService pokrService;
    @PostMapping("")
    public Pokrovitelj createPokrovitelj (@RequestParam("url") String url,@RequestParam("naziv") String naziv, @RequestParam("logo") MultipartFile logo) throws IOException {
        Pokrovitelj pokrovitelj = new Pokrovitelj();
        pokrovitelj.setUrl(url);
        pokrovitelj.setNaziv(naziv);
        return pokrService.createPokrovitelj(pokrovitelj, logo);
    }

    /*@GetMapping("")
    public String createAdmin(){
        return "sponzor";
    }*/
}
