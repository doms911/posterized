package hr.fer.progi.posterized.rest;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@Profile("basic-security")
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
public class WebSecurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    //@Bean
    //@Order(Ordered.HIGHEST_PRECEDENCE)
    //public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.securityMatcher(PathRequest.toH2Console());
        //http.csrf(AbstractHttpConfigurer::disable);
        //http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        //return http.build();
    //}

}
