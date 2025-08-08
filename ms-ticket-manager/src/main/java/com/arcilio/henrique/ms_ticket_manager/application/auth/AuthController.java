package com.arcilio.henrique.ms_ticket_manager.application.auth;

import com.arcilio.henrique.ms_ticket_manager.application.representation.account.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.SignInCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.TokenDto;
import jakarta.validation.Valid;
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
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInCredentialsDto credentials){
        return authService.signIn(credentials);
    }
    @PostMapping("/create-user")
    public ResponseEntity<Void> createUser(@Valid @RequestBody AccountCredentialsDto credentials){
        authService.createUser(credentials);
        return ResponseEntity.noContent().build();
    }

}
