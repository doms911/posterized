package hr.fer.progi.posterized.service.impl;


import hr.fer.progi.posterized.dao.PasswordTokenRepository;
import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.PasswordToken;
import hr.fer.progi.posterized.service.PasswordTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
@Service
public class PasswordTokenServiceJPA implements PasswordTokenService {
    @Autowired
    private PasswordTokenRepository passwordTokenRepo;
    @Override
    public void createPasswordResetToken(Osoba osoba, String token) {
        Assert.notNull(osoba, "Osoba ne postoji.");
        PasswordToken myToken = passwordTokenRepo.findByOsoba_id(osoba.getId());
        if (myToken ==null) {
            myToken = new PasswordToken(token, osoba);
        }
        else{
            myToken.updateToken(token);
        }
        passwordTokenRepo.save(myToken);
    }
    public PasswordTokenServiceJPA(){

    }
    public PasswordTokenServiceJPA(PasswordTokenRepository ptr){
        this.passwordTokenRepo=ptr;
    }

    @Override
    public PasswordToken findByToken(String token) {
        return passwordTokenRepo.findByToken(token);
    }

    @Override
    public Optional<Osoba> getOsobaByPasswordResetToken(String token){
        return Optional.ofNullable(passwordTokenRepo.findByToken(token).getOsoba());
    }

    @Override
    @Transactional
    public void deleteByToken(String token){
        passwordTokenRepo.deleteByToken(token);
    }
}
