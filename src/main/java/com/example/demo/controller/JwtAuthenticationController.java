package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.JWTAuthenticationConfig;
import com.example.demo.security.JwtRequest;
import com.example.demo.security.JwtResponse;

@Tag(name = "Jwt authentication")
@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Operation(summary = "Autenticarse: devuelve el token JWT", security = {})
    @RequestMapping(
        value = "/authenticate",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        if (!userDetails.getPassword().equals(authenticationRequest.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o password incorrectos");
        }

        final String token = jwtAuthenticationConfig.getJWTToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
