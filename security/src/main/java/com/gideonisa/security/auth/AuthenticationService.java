package com.gideonisa.security.auth;

import com.gideonisa.security.config.JwtService;
import com.gideonisa.security.user.Role;
import com.gideonisa.security.user.User;
import com.gideonisa.security.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registering a user an generating a token for that user
     * @param request
     * @return
     */
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticating an exiting user in our database and given it a session token
     * @param request
     * @return
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // This is the point where the request is authenticated
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

        }catch (AuthenticationException auth) {
            auth.getMessage();
        }

        User user = repository.findByEmail(request.getEmail()).orElseThrow();

        //Generating a jwt token for this authenticated user
        String jwtToken = jwtService.generateToken(user);


        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
