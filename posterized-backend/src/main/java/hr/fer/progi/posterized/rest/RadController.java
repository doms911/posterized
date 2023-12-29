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

import java.util.List;

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
                          @RequestParam("pptx") MultipartFile pptx,
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

    @GetMapping("/izbrisi/{naslov}")
    public void izbrisiRad (@PathVariable("naslov") String naslov, @AuthenticationPrincipal User user){
        radService.izbrisiRad(user.getUsername(), naslov);
    }
}
