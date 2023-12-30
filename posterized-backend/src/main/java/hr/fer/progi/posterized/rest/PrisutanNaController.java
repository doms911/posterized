package hr.fer.progi.posterized.rest;


import hr.fer.progi.posterized.service.PrisutanNaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prisutan")
public class PrisutanNaController {
    @Autowired
    PrisutanNaService prisService;
    @GetMapping("/{pin}")
    public List<Map<String, String>> provjeriPin(@PathVariable("pin") String pin, @AuthenticationPrincipal User user){
        return prisService.provjeriPin(user.getUsername(), Integer.valueOf(pin));
    }
    @GetMapping("/glasaj/{naslov}")
    public void glasaj (@PathVariable("naslov") String naslov, @AuthenticationPrincipal User user){
        prisService.glasaj(user.getUsername(), naslov);
    }
    @Secured("admin")
    @GetMapping("/zavrsiKonf/{naziv}")
    public void zavrsiKonf(@PathVariable("naziv") String nazivKonf, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        prisService.saljiMail(email, nazivKonf);
    }
}
