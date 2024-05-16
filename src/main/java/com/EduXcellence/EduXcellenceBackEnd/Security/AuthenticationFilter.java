package com.EduXcellence.EduXcellenceBackEnd.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthenticationFilter {

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
}



