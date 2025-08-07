package com.arcilio.henrique.ms_ticket_manager.application.auth;

import com.arcilio.henrique.ms_ticket_manager.application.exception.security.UsernameAlreadyExistsException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.SignInCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.TokenDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserRepository;
import com.arcilio.henrique.ms_ticket_manager.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public ResponseEntity<TokenDto> signIn(    SignInCredentialsDto signInCredentials){
        authenticationManager.authenticate
             (             new UsernamePasswordAuthenticationToken(
                     signInCredentials.getUsername(), signInCredentials.getPassword()));
        User user = userRepository.findByUsername(signInCredentials.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }
        TokenDto tokenDto = tokenProvider.createAcessToken(signInCredentials.getUsername(), List.of(user.getRole()));
        return ResponseEntity.ok(tokenDto);
    }

    public void createUser(AccountCredentialsDto credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        if(user != null){
            throw new UsernameAlreadyExistsException(
                            "The username " +
                            credentials.getUsername() +
                            " is already in use");
        }
        User newUser = new User();
        newUser.setUsername(credentials.getUsername());
        newUser.setPassword(encriptPassword(credentials.getPassword()));
        newUser.setFullname(credentials.getFullname());
        newUser.setCpf(credentials.getCpf());
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        newUser.setRole("ROLE_CUSTOMER");
        userRepository.save(newUser);
    }

    private String encriptPassword(String password){
        return encoder.encode(password);
    }
}
