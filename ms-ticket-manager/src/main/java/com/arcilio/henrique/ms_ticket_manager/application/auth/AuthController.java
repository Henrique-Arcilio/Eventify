package com.arcilio.henrique.ms_ticket_manager.application.auth;

import com.arcilio.henrique.ms_ticket_manager.application.representation.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody AccountCredentialsDto credentials){
        return authService.signIn(credentials);
    }
    @PostMapping("/create-user")
    public ResponseEntity<Void> createUser(@RequestBody AccountCredentialsDto credentials){
        authService.createUser(credentials);
        return ResponseEntity.noContent().build();
    }

}
