package hr.fer.progi.posterized.dao;

import hr.fer.progi.posterized.domain.PrisutanNaKljuc;
import hr.fer.progi.posterized.domain.Prisutan_na;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrisutanNaRepository extends JpaRepository<Prisutan_na, PrisutanNaKljuc> {
    Optional<Prisutan_na> findById(PrisutanNaKljuc kljuc);
}
