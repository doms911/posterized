package hr.fer.progi.posterized.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FotografijaService {
    void spremiSlike(String nazivKonf, String admin, List<MultipartFile> slike);
    List<String> dohvatiSlike(Integer pin);
}
