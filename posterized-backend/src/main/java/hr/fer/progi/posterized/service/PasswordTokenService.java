package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.PasswordToken;

import java.util.Optional;

public interface PasswordTokenService {
    void createPasswordResetToken(Osoba osoba, String token);
    PasswordToken findByToken(String token);
    Optional<Osoba> getOsobaByPasswordResetToken(String token);
    void deleteByToken(String token);
}
