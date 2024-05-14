package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Administrateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Repository.ParticipantRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ServiceAuthentification {

    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private final MongoTemplate mongoTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String KEY = "T25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNl";
    private boolean p;
    private String token;


    public ServiceAuthentification(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String login(Participant participant) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(participant.getEmail()));
        Participant user = mongoTemplate.findOne(query, Participant.class);
        if (user == null || !bCryptPasswordEncoder.matches(participant.getMotDePasse(), user.getMotDePasse()) || user.getRole() != "USER") {
            throw new RuntimeException("Invalid email or password");}
        return  Jwts.builder()
                .setSubject(participant.getEmail())
                .claim("id", user.getId())
                .claim("NomPrenom", user.getNomPrenom())
                .claim("NiveauDEtude", user.getNiveauDEtude())
                .claim("Role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String getKEY() {
        return KEY;
    }


    public ResponseEntity<Map<String,String>> inscriptionParticipant(Participant participant) {
        Map <String,String>map = new HashMap<>();
            Query query = new Query();
            Query existEmail = query(Criteria.where("email").regex(participant.getEmail(), "i"));
            Long part =  mongoTemplate.count(existEmail,Participant.class);
            if (part == 0 ){
            String encryptedPassword = bCryptPasswordEncoder.encode(participant.getMotDePasse());
            participant.setMotDePasse(encryptedPassword);
            this.participantRepo.save(participant);
            map.put("Message","Ajouté avec Succés");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("Message","Email existe Déjà");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    public boolean isAuthenticated(String token){
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        return authenticationFilter.VerifierTOKEN(token);
    }

    public ResponseEntity<Map<String,String>> loginAdmin(Administrateur administrateur) {
        Map <String,String>map = new HashMap<>();
        Query existid =new Query().addCriteria(Criteria.where("id").is(administrateur.getId()));
         Administrateur administrateur1 = mongoTemplate.findOne(existid, Administrateur.class);
        if (administrateur1 == null || !bCryptPasswordEncoder.matches(administrateur.getMotDePasse(), administrateur1.getMotDePasse()) || administrateur1.getRole() != "ADMIN") {
        map.put("Message","Invalid ID or password");
        }else {
            token = Jwts.builder()
                    .setSubject(administrateur.getId())
                    .claim("id", administrateur1.getId())
                    .claim("Role", administrateur1.getRole())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                    .signWith(SignatureAlgorithm.HS256, KEY)
                    .compact();
            map.put("Message", "Bonjour "+administrateur1.getNomPrenom());
            map.put("Token",token);
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    public ResponseEntity<Map<String,String>> loginFormateur(Formateur formateur) {
        Map <String,String>map = new HashMap<>();
        Query query = new Query();
        Query existid = query.addCriteria(Criteria.where("id").is(formateur.getId()));
        Query existrole = query.addCriteria(Criteria.where("Role").is("USER"));
        Formateur formateur1 = mongoTemplate.findOne(query, Formateur.class);
        if (formateur1 == null || !bCryptPasswordEncoder.matches(formateur.getMotDePasse(), formateur1.getMotDePasse()) || formateur1.getRole() != "USER") {
            map.put("Message","Invalid email or password");
        }else {
            token = Jwts.builder()
                    .setSubject(formateur.getId())
                    .claim("id", formateur1.getId())
                    .claim("Role", formateur1.getRole())
                    .claim("NomPrenom", formateur1.getNomPrenom())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                    .signWith(SignatureAlgorithm.HS256, KEY)
                    .compact();
            map.put("Message", "Bonjour "+formateur1.getNomPrenom());
            map.put("Token",token);
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

}




