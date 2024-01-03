package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Mjesto;
import hr.fer.progi.posterized.service.MjestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/addPlace")
public class MjestoController {

    @Autowired
    private MjestoService mService;

    @PostMapping("")
    public Mjesto createMjesto(Integer pbr, String Naziv){
        return mService.createMjesto(pbr, Naziv);
    }

}
