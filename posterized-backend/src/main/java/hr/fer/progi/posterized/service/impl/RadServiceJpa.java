package hr.fer.progi.posterized.service.impl;

import hr.fer.progi.posterized.dao.RadRepository;
import hr.fer.progi.posterized.domain.Rad;
import hr.fer.progi.posterized.service.RadService;
import hr.fer.progi.posterized.service.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class RadServiceJpa implements RadService {

    @Autowired
    private RadRepository radRepo;
    @Override
    public List<Rad> listAll() {
        return radRepo.findAll();
    }



    @Override
    public Rad createRad(Rad rad) {
        Assert.notNull(rad,"Rad object must be given");
        Assert.isNull(rad.getId(), "Rad ID must be null, not" + rad.getId());


        if(radRepo.countByImeRada(rad.getImeRada())>0){
            throw new RequestDeniedException("Rad with name " + rad.getImeRada() + " already exits");
        }

        return radRepo.save(rad);
    }
}
