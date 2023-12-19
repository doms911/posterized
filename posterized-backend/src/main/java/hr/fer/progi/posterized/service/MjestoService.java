package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Mjesto;

import java.util.List;

public interface MjestoService {

    List<Mjesto> listAll();

    public Mjesto createMjesto(Integer pbr, String naziv);

}
