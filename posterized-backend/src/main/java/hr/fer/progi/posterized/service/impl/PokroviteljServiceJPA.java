package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.PokroviteljRepository;
import hr.fer.progi.posterized.domain.Pokrovitelj;
import hr.fer.progi.posterized.domain.Slika;
import hr.fer.progi.posterized.service.PokroviteljService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PokroviteljServiceJPA implements PokroviteljService {
    @Autowired
    private PokroviteljRepository pokroviteljRepo;

    @Override
    public Pokrovitelj createPokrovitelj(Pokrovitelj pokrovitelj, MultipartFile logo) throws IOException {
        Assert.notNull(pokrovitelj, "Pokrovitelj object must be given");
        String naziv = pokrovitelj.getNaziv();
        Assert.hasText(naziv, "Naziv must be given");
        String url = pokrovitelj.getUrl();
        Assert.hasText(url, "Url must be given");
        if(logo.isEmpty()) Assert.hasText("", "Logo must be given");
        if (pokroviteljRepo.countByNazivCaseInsensitive(pokrovitelj.getNaziv()) > 0) {
            Assert.hasText("", "Pokrovitelj with naziv " + pokrovitelj.getNaziv() + " already exists");
        }
        if (pokroviteljRepo.countByUrl(pokrovitelj.getUrl()) > 0) {
            Assert.hasText("", "Pokrovitelj with url " + pokrovitelj.getNaziv() + " already exists");
        }
        Slika slika = new Slika();
        pokrovitelj.setUrlSlike(slika.upload(logo, naziv, "pokrovitelji"));
        return pokroviteljRepo.save(pokrovitelj);
    }

    @Override
    public List<Pokrovitelj> listAll(){
        return pokroviteljRepo.findAll();
    }

    @Override
    public Pokrovitelj findByNazivIgnoreCase(String naziv){return pokroviteljRepo.findByNazivIgnoreCase(naziv);}
}
