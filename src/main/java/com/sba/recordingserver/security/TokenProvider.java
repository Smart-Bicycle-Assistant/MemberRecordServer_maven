package com.sba.recordingserver.security;

import com.sba.recordingserver.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(Member member) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .setSubject(member.getId())
                .setIssuer("S-BA")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public static String GetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
