package hr.fer.progi.posterized.service;


import hr.fer.progi.posterized.domain.Pokrovitelj;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PokroviteljService {
    Pokrovitelj createPokrovitelj(Pokrovitelj pokrovitelj, MultipartFile slika) throws IOException;
    List<Pokrovitelj> listAll();
    Pokrovitelj findByNazivIgnoreCase(String sponzor);
}
