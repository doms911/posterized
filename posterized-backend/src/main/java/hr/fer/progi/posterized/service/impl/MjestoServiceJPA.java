package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.MjestoRepository;
import hr.fer.progi.posterized.domain.Mjesto;
import hr.fer.progi.posterized.service.MjestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class MjestoServiceJPA implements MjestoService {

    @Autowired
    private MjestoRepository mjestoRepo;

    @Override
    public List<Mjesto> listAll() {
        return mjestoRepo.findAll();
    }

    @Override
    public Mjesto createMjesto(Integer pbr, String naziv) {

        Assert.notNull(pbr,"Poštanski broj mora biti naveden.");

        if(mjestoRepo.countByPbr(pbr)>0){
            Assert.hasText("","Zapis o ovom mjestu već postoji.");
        }
        Mjesto mjesto = new Mjesto();
        mjesto.setPbr(pbr);
        mjesto.setNaziv(naziv);

        return mjestoRepo.save(mjesto);
    }

    @Override
    public Mjesto findByPbr(Integer pbr) {
        return mjestoRepo.findByPbr(pbr);
    }

    @Override
    public Mjesto update(String naziv, Integer pbr){
        Mjesto mj = mjestoRepo.findByPbr(pbr);
        Assert.notNull(mj,"Mjesto ne postoji.");
        mj.setNaziv(naziv);
        return mjestoRepo.save(mj);
    }
}
