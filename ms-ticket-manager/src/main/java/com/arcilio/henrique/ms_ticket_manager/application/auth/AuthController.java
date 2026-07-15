package com.arcilio.henrique.ms_ticket_manager.application.auth;

import com.arcilio.henrique.ms_ticket_manager.application.docs.AuthResourceDocs;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.SignInCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication")
public class AuthController implements AuthResourceDocs {

    private final AuthService authService;



    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInCredentialsDto credentials){
        return authService.signIn(credentials);
    }
    @PostMapping("/create-user")
    @Override
    public ResponseEntity<Void> createUser(@Valid @RequestBody AccountCredentialsDto credentials){
        authService.createUser(credentials);
        return ResponseEntity.noContent().build();
    }

}
