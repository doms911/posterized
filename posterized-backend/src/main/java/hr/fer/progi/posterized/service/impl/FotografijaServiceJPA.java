package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.FotografijaRepository;
import hr.fer.progi.posterized.domain.Fotografija;
import hr.fer.progi.posterized.domain.Konferencija;
import hr.fer.progi.posterized.domain.Media;
import hr.fer.progi.posterized.service.FotografijaService;
import hr.fer.progi.posterized.service.KonferencijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class FotografijaServiceJPA implements FotografijaService {
    @Autowired
    KonferencijaService konfService;
    @Autowired
    FotografijaRepository fotoRepo;

    @Override
    public void spremiSlike(String nazivKonf, String admin, List<MultipartFile> slike){
        Konferencija konf = konfService.findByNazivIgnoreCase(nazivKonf);
        if(konf == null) Assert.hasText("","Konferencija with naziv " + nazivKonf + " does not exists");
        if(!konf.getAdminKonf().getEmail().equalsIgnoreCase(admin)) Assert.hasText("","You do not have access to this conference.");
        Media objekt = new Media();
        for(MultipartFile slika: slike){
            String naziv = UUID.randomUUID().toString();
            String url = objekt.upload(slika, naziv, nazivKonf+"/fotografije");
            Fotografija foto = new Fotografija();
            foto.setKonferencija(konf);
            foto.setUrlSlike(url);
            fotoRepo.save(foto);
        }
    }
}
