package hr.fer.progi.posterized;

import hr.fer.progi.posterized.dao.*;
import hr.fer.progi.posterized.domain.*;
import hr.fer.progi.posterized.service.*;
import hr.fer.progi.posterized.service.impl.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PosterizedApplicationTests {
	@Mock
	OsobaRepository osobaRepository;
	@Mock
	MjestoRepository mjestoRepository;
	@Mock
	KonferencijaRepository konferencijaRepository;
	@Mock
	PasswordTokenRepository passwordTokenRepository;


	OsobaService os = new OsobaServiceJPA(osobaRepository);


	//testira pozive repozitoriju pri stvaranje novog autora
	@Test
	public void testCreateAutor() {
		Osoba o = new Osoba();
		o.setIme("ana");
		o.setPrezime("bozic");
		o.setUloga("zaposlenik");
		o.setEmail("abc@def.com");
		os = new OsobaServiceJPA(osobaRepository);

		when(osobaRepository.countByEmail(o.getEmail())).thenReturn(0);
		when(osobaRepository.save(o)).thenReturn(o);

		os.createAutor(o);

		verify(osobaRepository, times(1)).countByEmail(o.getEmail());
		verify(osobaRepository, times(1)).save(o);

	}

	//testiranje createAutor s null osobom
	@Test
	public void testCreateNullAutor(){

		assertThrows(IllegalArgumentException.class, () -> os.createAutor(null));
	}

	//testiranje osobe bez emaila
	@Test
	public void testCreateAutorNoEmail(){
		Osoba o = new Osoba();
		o.setIme("ana");
		o.setPrezime("bozic");
		o.setUloga("zaposlenik");
		assertThrows(IllegalArgumentException.class, () -> os.createAutor(o));
	}

	//testirat dodavanja mjesta koje vec postoji
	@Test
	public void testAlreadyExistingPlace(){
		Mjesto m=new Mjesto();
		m.setNaziv("Zapresic");
		m.setPbr(10290);

		MjestoService ms = new MjestoServiceJPA(mjestoRepository);

		when(mjestoRepository.countByPbr(10290)).thenReturn(1);

		assertThrows(IllegalArgumentException.class,() -> ms.createMjesto(10290,"zapresic"));
		verify(mjestoRepository,times(1)).countByPbr(10290);

	}

 	//stvaranje koferencije koja vec postoji

	@Test
	public void testCreateConference(){
		KonferencijaService ks=new KonferencijaServiceJPA(konferencijaRepository);
		Konferencija k = new Konferencija();
		k.setPin(1234);
		k.setAdresa("random@email");
		k.setNaziv("fer");

		when(konferencijaRepository.countByPin(k.getPin())).thenReturn(1);

		assertThrows(IllegalArgumentException.class, ()-> ks.createKonferencija(k.getPin().toString(),k.getAdresa(),k.getNaziv()));
		verify(konferencijaRepository,times(1)).countByPin(k.getPin());
	}

	//davanje password tokena nepostojecoj osobi
	@Test
	public void testPasswordTokenNoId(){
		PasswordTokenService pts=new PasswordTokenServiceJPA(passwordTokenRepository);
		Osoba o=new Osoba();
		o.setId(123L);
		PasswordToken pt=new PasswordToken("123");

		when(passwordTokenRepository.findByOsoba_id(o.getId())).thenReturn(pt);
		when(passwordTokenRepository.save(pt)).thenReturn(pt);

		pts.createPasswordResetToken(o,pt.getToken());

		verify(passwordTokenRepository,times(1)).findByOsoba_id(o.getId());
		verify(passwordTokenRepository,times(1)).save(pt);






	}

}


