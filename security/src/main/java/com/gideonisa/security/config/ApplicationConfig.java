package com.gideonisa.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gideonisa.security.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            return repository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
            // TODO Auto-generated method stub
            //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
        };
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                return repository
//                        .findByEmail(username)
//                        .orElseThrow(() -> new UsernameNotFoundException(username));
//                // TODO Auto-generated method stub
//                //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
//            }
//
//        };
//    }

    /**
     * Manages the authentications
     * @param config
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return  config.getAuthenticationManager();
    }


    /**
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider is one of a contract type of the interface AuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // We set where the provider should get the user's info
        authenticationProvider.setUserDetailsService(userDetailsService());
        // We set what type of password encoding we are using
        authenticationProvider.setPasswordEncoder(passwordEncoder());


        System.out.println();

        return authenticationProvider;
    }



    /**
     * Seeting the password encryption type
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
