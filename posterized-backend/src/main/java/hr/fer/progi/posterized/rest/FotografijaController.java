package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.service.FotografijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/preuzmi")
    public ResponseEntity<byte[]> preuzmiSliku(@RequestParam String url) {
        try {
            byte[] imageBytes = fotoService.preuzmi(url);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/dohvatiSlike/{pin}")
    public List<String> dohvatiSlike(@PathVariable("pin") String pin){
        return fotoService.dohvatiSlike(Integer.valueOf(pin));
    };
}
