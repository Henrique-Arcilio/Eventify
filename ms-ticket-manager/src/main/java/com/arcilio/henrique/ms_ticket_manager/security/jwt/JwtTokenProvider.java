package com.arcilio.henrique.ms_ticket_manager.security.jwt;

import com.arcilio.henrique.ms_ticket_manager.application.representation.TokenDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secrect}")
    private String secretKey = "secret";
    @Value("${spring.security.jwt.token.expire-length:3600000}")
    private long expirationTimeMilis = 36000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDto createAcessToken(String username, List<String> roles){
        Date now = new Date();

        Date expiration = new Date(now.getTime() + expirationTimeMilis);
        String acessToken = getAcessToken(username, roles, now, expiration);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDto(username, true, now, expiration, acessToken, refreshToken);
    }

    private String getAcessToken(String username, List<String> roles, Date now, Date expiration) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm);
    }
    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date refreshTokenValidity = new Date(now.getTime() + expirationTimeMilis);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(username)
                .sign(algorithm);
    }

}
