package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.Rad;
import hr.fer.progi.posterized.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@Controller
@RequestMapping("/radovi")
public class RadController {

    @Autowired
    private RadService radService;

    /*@GetMapping("")
    public String createAdmin(){
        return "rad";
    }*/

    @PostMapping("/napravi")
    public void createRad( @AuthenticationPrincipal User user, @RequestParam("ime") String ime,
    @RequestParam("prezime") String prezime, @RequestParam("email") String email,
                          @RequestParam("naslov") String naslov, @RequestParam("poster") MultipartFile poster,
                          @RequestParam(value = "pptx", required = false) MultipartFile pptx,
                          @RequestParam("nazivKonf") String nazivKonf){
        Osoba autor = new Osoba();
        autor.setIme(ime);
        autor.setPrezime(prezime);
        autor.setEmail(email);
        autor.setUloga("autor");
        Rad rad = new Rad();
        rad.setNaslov(naslov);
        radService.createRad(user.getUsername(), autor, rad, poster, pptx, nazivKonf);
    }

    @Secured("admin")
    @PostMapping("/nadopuniRad/{stari_naslov}")
    public void updateRad(@PathVariable("stari_naslov") String stariNazivRad, @RequestParam("naslov") String nazivRada, @AuthenticationPrincipal User user,
                           @RequestParam("ime") String ime,
                           @RequestParam("prezime") String prezime,
                           @RequestParam("email") String email,
                           @RequestParam("poster") MultipartFile poster,
                           @RequestParam(value = "pptx", required = false) MultipartFile pptx,
                           @RequestParam("nazivKonf") String nazivKonf){
        radService.updateRad(user.getUsername(), nazivKonf, stariNazivRad, nazivRada, ime, prezime, email, poster, pptx);
    }

    @Secured("admin")
    @GetMapping("/{naslov}")
    public List<Map<String, String>> prikaziAdminuRad(@PathVariable("naslov") String naslov){
        Rad rad = radService.findByNaslovIgnoreCase(naslov);
        List<Map<String, String>> rezultat = new ArrayList<>();
        Map<String, String> radMapa = new HashMap<>();
        radMapa.put("naslov", rad.getNaslov());
        radMapa.put("ukupnoGlasova", String.valueOf(rad.getUkupnoGlasova()));
        radMapa.put("urlPptx", rad.getUrlPptx());
        radMapa.put("urlPoster", rad.getUrlPoster());
        Osoba autor = rad.getAutor();
        radMapa.put("ime", autor.getIme());
        radMapa.put("prezime", autor.getPrezime());
        radMapa.put("mail", autor.getEmail());
        rezultat.add(radMapa);
        return rezultat;
    }

    @GetMapping("/izbrisi/{naslov}")
    public void izbrisiRad (@PathVariable("naslov") String naslov, @AuthenticationPrincipal User user){
        radService.izbrisiRad(user.getUsername(), naslov);
    }
    @GetMapping("/plasman/{naziv}")
    public void plasman (@PathVariable("naziv") String naziv){
        radService.plasman(naziv);
    }
}
