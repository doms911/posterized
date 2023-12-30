package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.FotografijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fotografija")
public class FotografijaController {
    @Autowired
    FotografijaService fotoService;
    @PostMapping("/{naziv}")
    public void dodajSlike(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user,
                           @RequestParam("slike") List<MultipartFile> slike){
        fotoService.spremiSlike(nazivKonf, user.getUsername(), slike);

    }

    @GetMapping("/dohvatiSlike/{pin}")
    public List<String> dohvatiVideo(@PathVariable("pin") String pin){
        return fotoService.dohvatiSlike(Integer.valueOf(pin));
    };
}
