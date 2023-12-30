package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.domain.Rad;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RadService {
    List<Rad> listAll();
    void createRad(String user, Osoba autor, Rad rad, MultipartFile poster, MultipartFile pptx, String nazivKonf);
    @Transactional
    void izbrisiRad(String admin, String naziv);
    Rad findByNaslovIgnoreCase(String naslov);
}
