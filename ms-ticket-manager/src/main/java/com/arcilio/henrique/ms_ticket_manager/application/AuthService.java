package com.arcilio.henrique.ms_ticket_manager.application;

import com.arcilio.henrique.ms_ticket_manager.application.representation.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.TokenDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserRepository;
import com.arcilio.henrique.ms_ticket_manager.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public ResponseEntity<TokenDto> signIn(    AccountCredentialsDto credentialsDto){
        authenticationManager.authenticate
             (             new UsernamePasswordAuthenticationToken(
                     credentialsDto.getUsername(), credentialsDto.getPassword()));
        User user = userRepository.findByUsername(credentialsDto.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }
        TokenDto tokenDto = tokenProvider.createAcessToken(credentialsDto.getUsername(), List.of("CLIENTE"));
        return ResponseEntity.ok(tokenDto);
    }
}
