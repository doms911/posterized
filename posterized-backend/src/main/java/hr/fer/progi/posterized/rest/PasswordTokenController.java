package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.PasswordToken;
import hr.fer.progi.posterized.service.OsobaService;
import hr.fer.progi.posterized.service.PasswordTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;

@RestController
@RequestMapping("/reset")
public class PasswordTokenController {
    @Autowired
    private OsobaService akService;
    @Autowired
    private PasswordTokenService passService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment env;

    @PostMapping("/resetLozinka")
    public Map<String, String> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        userEmail = userEmail.toLowerCase();
        Osoba osoba = akService.findByEmail(userEmail);
        Map<String, String> response = new HashMap<>();
        if (osoba == null) {
            String message = "Ne postoji korisnik " + userEmail;
            Assert.hasText("", message);
        }
        String token = UUID.randomUUID().toString();
        passService.createPasswordResetToken(osoba, token);
        mailSender.send(napraviEmail(env.getProperty("send.email.link"), token, osoba));
        response.put("message", "Ponovno postavi lozinku");
        return response;
    }

    private SimpleMailMessage napraviEmail(String contextPath, String token, Osoba osoba) {
        final String url = contextPath + "/changePassword?id=" + osoba.getId() + "&token=" + token;
        final String message = "Ponovno postavi lozinku";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(osoba.getEmail());
        email.setSubject("Ponovno postavljanje lozinke");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    @PostMapping("/promijeniLozinku")
    public String provjeriToken(@RequestParam("token") String token) {
        String result = validatePasswordResetToken(token);
        if(result != null) {
            String message = "Token za ponovno postavljanje lozinke nije valjan.";
            Assert.hasText("", message);
        }
        return "";
    }

    private String validatePasswordResetToken(String token) {
        final PasswordToken passToken = passService.findByToken(token);
        return passToken == null ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }
    private boolean isTokenExpired(PasswordToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getIstek().before(cal.getTime());
    }

    @PostMapping("/spremiLozinku")
    public Map<String, String> spremiLozinku(@RequestParam("token") String token, @RequestParam("lozinka") String lozinka) {
        Map<String, String> response = new HashMap<>();
        String result = validatePasswordResetToken(token);
        if(result != null) {
            Assert.hasText("", "Token za ponovno postavljanje lozinke nije valjan.");
        }
        Optional<Osoba> osoba = passService.getOsobaByPasswordResetToken(token);
        if(osoba.isPresent()) {
            akService.promijeniOsobiLozinku(osoba.get(), lozinka, token);
            passService.deleteByToken(token);
            response.put("message", "Lozinka je uspješno postavljena.");
        }
        return response;
    }
}
