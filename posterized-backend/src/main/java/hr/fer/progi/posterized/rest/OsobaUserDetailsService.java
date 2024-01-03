package hr.fer.progi.posterized.rest;

import hr.fer.progi.posterized.domain.Osoba;
import hr.fer.progi.posterized.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class OsobaUserDetailsService implements UserDetailsService {
    @Value("${hr.fer.progi.posterized.admin.password}")
    private String superadminLozinkaHash;

    @Autowired
    private OsobaService akService;
    @Override
    public UserDetails loadUserByUsername(String username) {
        if ("superadmin".equals(username)) {
            return new User(
                    username,
                    superadminLozinkaHash,
                    commaSeparatedStringToAuthorityList("superadmin")
            );}
        Osoba korisnik = akService.findByEmail(username.toLowerCase());
        if (korisnik != null) {
            return new User(
                    korisnik.getEmail(),
                    korisnik.getLozinka(),
                    commaSeparatedStringToAuthorityList(korisnik.getUloga())
            );
        } else {
            throw new UsernameNotFoundException("No user " + username);
        }
    }
}

