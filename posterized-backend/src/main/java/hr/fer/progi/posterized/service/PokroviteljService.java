package hr.fer.progi.posterized.service;


import hr.fer.progi.posterized.domain.Pokrovitelj;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PokroviteljService {
    public Pokrovitelj createPokrovitelj(Pokrovitelj pokrovitelj, MultipartFile slika) throws IOException;
}
