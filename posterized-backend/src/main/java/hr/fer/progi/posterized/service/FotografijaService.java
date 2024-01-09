package hr.fer.progi.posterized.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FotografijaService {
    void spremiSlike(String nazivKonf, String admin, List<MultipartFile> slike);
    List<String> dohvatiSlike(Integer pin);

    byte[] preuzmi(String url) throws IOException;
}
