package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.PokroviteljRepository;
import hr.fer.progi.posterized.domain.Pokrovitelj;
import hr.fer.progi.posterized.domain.Media;
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
        Assert.notNull(pokrovitelj, "Podaci o pokrovitelju moraju biti navedeni.");
        String naziv = pokrovitelj.getNaziv();
        Assert.hasText(naziv, "Naziv mora biti naveden.");
        String url = pokrovitelj.getUrl();
        Assert.hasText(url, "URL mora biti naveden.");
        if(logo.isEmpty()) Assert.hasText("", "Logo mora biti učitan.");
        if (pokroviteljRepo.countByUrlCaseInsensitive(pokrovitelj.getUrl()) > 0) {
            Assert.hasText("", "Pokrovitelj s URL-om '" + pokrovitelj.getUrl() + "' već postoji pod nazivom: '" +
                    pokroviteljRepo.findByUrlIgnoreCase(pokrovitelj.getUrl()).getNaziv() + "'.");
        }
        if (pokroviteljRepo.countByNazivCaseInsensitive(pokrovitelj.getNaziv()) > 0) {
            Assert.hasText("", "Pokrovitelj s nazivom '" + pokrovitelj.getNaziv() + "' već postoji.");
        }
        Media slika = new Media();
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
