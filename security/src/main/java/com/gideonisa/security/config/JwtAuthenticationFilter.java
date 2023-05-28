package com.gideonisa.security.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
        ) throws ServletException, IOException {

            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            
            // the authorization always starts with the keyword BEARER
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // passing the request to the next filter
                filterChain.doFilter(request, response);
                return;
            }

            //Extracting the Token from the Authorization Header
            jwt = authHeader.substring(7); 
            // starting from 7 because "BEARER " of the auth. is seven
            // so cutting from the character after the white space in-front
            // of the word "BEARER "

            //Extracting the user's email from the jwt token
            userEmail = jwtService.extractUsername(jwt);//
            
            // userEmail != null --- checks if user's email(username) was received
            // SecurityContextHolder.getContext().getAuthentication() == null --- checks if the user has already
            // been authenticated. if not yet authenticated, it will evaluate to null
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // retrieving the user's details user the concrete interface of UserDetails which our User model has extended
               UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);

               if (jwtService.isTokenValid(jwt, userDetails)) {
                   // if the user is valid, we create a UsernamePasswordAuthenticationToken variable to store the
                   // user into it.
                // This object "authtoken" is needed by spring to update the security context
                UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(
                         userDetails,
                        null,
                        userDetails.getAuthorities());

                    // we enforce the authtoken with the request to the spring context
                    authtoken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    //updating the Security Context Holder 
                    SecurityContextHolder.getContext().setAuthentication(authtoken);

               }
               
            }

            // We need always to pass the it to the next filter
            // This passes it to the next filter
            filterChain.doFilter(request, response);


        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }
    
}
