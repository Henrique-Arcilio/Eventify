package com.arcilio.henrique.ms_ticket_manager.security.jwt;

import com.arcilio.henrique.ms_ticket_manager.application.exception.security.InvalidJwtAuthenticationException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.TokenDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expirationTimeMilis;

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
        Date refreshTokenValidity = new Date(now.getTime() + expirationTimeMilis * 3);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(username)
                .sign(algorithm);
    }
    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        try {
            Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
            JWTVerifier verifier = JWT.require(alg).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch(Exception e){
            throw new InvalidJwtAuthenticationException("Expired or invalid token");
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }else{
            return null;
        }

    }

    public boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Expired or invalid token");
        }
    }
}
