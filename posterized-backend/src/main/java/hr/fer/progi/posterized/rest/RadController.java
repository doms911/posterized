package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Rad;
import hr.fer.progi.posterized.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/radovi")
public class RadController {

    @Autowired
    private RadService radService;

    @GetMapping("")
    public List<Rad> listRadovi(){
        return radService.listAll();
    }

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public Rad createRad(@RequestBody Rad rad){
        return radService.createRad(rad);
    }
}
