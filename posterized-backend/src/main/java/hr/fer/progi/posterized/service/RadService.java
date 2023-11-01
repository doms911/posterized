package hr.fer.progi.posterized.service;

import hr.fer.progi.posterized.domain.Rad;

import java.util.List;

public interface RadService {
    List<Rad> listAll();

    Rad createRad(Rad rad);
}
