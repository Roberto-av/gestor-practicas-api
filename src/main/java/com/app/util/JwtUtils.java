package com.app.util;

import com.app.persistence.entities.users.InvitationTokenEntity;
import com.app.persistence.repositories.InvitationTokenRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${securiry.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    @Autowired
    private InvitationTokenRepository invitationTokenRepository;

    public String createToken(Authentication auth) {

        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String username = (String) auth.getPrincipal();

        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7200000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalid");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Map<String, Claim> returnAllClaims (DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

    public String generateInvitationToken(String email) {
        String token = UUID.randomUUID().toString(); // Usar UUID para generar un token único

        InvitationTokenEntity invitationToken = new InvitationTokenEntity();
        invitationToken.setToken(token);
        invitationToken.setEmail(email);
        invitationToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000)); // 1 hour

        invitationTokenRepository.save(invitationToken);

        return token;
    }

    public String validateInvitationToken(String token) {
        Optional<InvitationTokenEntity> optionalToken = invitationTokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {
            InvitationTokenEntity invitationToken = optionalToken.get();
            if (invitationToken.isUsed() || invitationToken.getExpiryDate().before(new Date())) {
                throw new RuntimeException("Token is invalid or expired");
            }
            return invitationToken.getEmail();
        } else {
            throw new RuntimeException("Token not found");
        }
    }

}
