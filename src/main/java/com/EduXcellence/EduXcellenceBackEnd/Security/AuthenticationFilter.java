package com.EduXcellence.EduXcellenceBackEnd.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter {

    private AES256TextEncryptor textEncryptor;

    @Value("${jasypt.encryptor.password}")
    private String password;


    public boolean VerifierTOKEN(String Token) {
        try {
            Jwts.parser().setSigningKey("T25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNl").parseClaimsJws(Token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String RecupererRole(String Token) {
        Claims claims = Jwts.parser().setSigningKey("T25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNl").parseClaimsJws(Token).getBody();
        return claims.get("Role", String.class);
    }


    public String encrypt(String text) {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(password);
        return textEncryptor.encrypt(text);
    }


    public String decrypt(String encryptedText) {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(password);
        return textEncryptor.decrypt(encryptedText);
    }
}