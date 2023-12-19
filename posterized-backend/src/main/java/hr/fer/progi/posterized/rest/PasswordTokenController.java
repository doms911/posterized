package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.PasswordToken;
import hr.fer.progi.posterized.service.AdminKorisnikService;
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
    private AdminKorisnikService akService;
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
            String message = "No user " + userEmail;
            Assert.hasText("", message);
        }
        String token = UUID.randomUUID().toString();
        passService.createPasswordResetToken(osoba, token);
        mailSender.send(napraviEmail("http://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath(), token, osoba));
        response.put("message", "Reset Password");
        return response;
    }

    private SimpleMailMessage napraviEmail(String contextPath, String token, Osoba osoba) {
        final String url = contextPath + "/reset/changePassword?id=" + osoba.getId() + "&token=" + token;
        final String message = "Reset Password";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(osoba.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    @PostMapping("/promijeniLozinku")
    public String provjeriToken(@RequestParam("token") String token) {
        String result = validatePasswordResetToken(token);
        if(result != null) {
            String message = "Your password reset token is no valid.";
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
            Assert.hasText("", "Your password reset token is no valid.");
        }
        Optional<Osoba> osoba = passService.getOsobaByPasswordResetToken(token);
        if(osoba.isPresent()) {
            akService.promijeniOsobiLozinku(osoba.get(), lozinka, token);
            passService.deleteByToken(token);
            response.put("message", "Password reset successfully");
        }
        return response;
    }
}
