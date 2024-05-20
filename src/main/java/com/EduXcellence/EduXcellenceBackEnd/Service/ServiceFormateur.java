package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Administrateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Repository.FormateurRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

import static javax.crypto.Cipher.SECRET_KEY;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ServiceFormateur {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FormateurRepo formateurRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    AuthenticationFilter authenticationFilter;

    private final String KEY = "T25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNl";

    private String token;

    Map map = new HashMap();

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> loginFormateur(Formateur formateur) {
        Query query = new Query();
        query.addCriteria(where("id").is(formateur.getId()));
        Formateur formateur1 = mongoTemplate.findOne(query, Formateur.class);
        if (formateur1 == null || !bCryptPasswordEncoder.matches(formateur.getMotDePasse(), formateur1.getMotDePasse())) {
            map.put("Message", "Invalid email or password");
        } else {
            token = Jwts.builder()
                    .setSubject(formateur.getId())
                    .claim("id", formateur1.getId())
                    .claim("Role", formateur1.getRole())
                    .claim("NomPrenom", formateur1.getNomPrenom())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                    .signWith(SignatureAlgorithm.HS256, KEY)
                    .compact();
            map.put("Message", "Bonjour " + formateur1.getNomPrenom());
            map.put("Token", token);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    /*-------------------------------Gestion des formateurs---------------------------------*/

    public ResponseEntity<Map> AjouterFormateur(Formateur F, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query();
            Query existEmail = query(where("email").is(F.getEmail()));
            Long part = mongoTemplate.count(existEmail, Formateur.class);
            if (part == 0) {
                String motdepasseCrypter = authenticationFilter.encrypt(F.getMotDePasse());
                F.setMotDePasse(motdepasseCrypter);
                formateurRepo.save(F);
                map.put("Message", "Noveau Formateur Ajouter Avec Suceé");
            } else {
                map.put("Message", "Cette Email Exite Deja");
            }
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerFormateurs(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("TableFormateur", this.formateurRepo.findAll());
        } else {
            map.put("Message", "Acceé refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerFormateursAciverOuDesactiver(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query();
            Long NbrFormateurActive = mongoTemplate.count(query.addCriteria(where("active").is("true")), Formateur.class);
            Long NbrFormateurDesactive = mongoTemplate.count(query.addCriteria(where("active").is("false")), Formateur.class);
            map.put("FormateurActive", NbrFormateurActive);
            map.put("FormateurDesactive", NbrFormateurDesactive);
        } else {
            map.put("Message", "Acceé refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerNombreDeFormateurs(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("NombreDeFormateurs", this.formateurRepo.count());
        } else {
            map.put("Message", "Acceé refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerUnSeulFormateur(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Formateur formateur = mongoTemplate.findOne(query(where("id").is(id)), Formateur.class);
            if (formateur != null) {
                String pass = formateur.getMotDePasse();
                formateur.setMotDePasse(authenticationFilter.decrypt(pass));
                map.put("Formateur", formateur);
            } else {
                map.put("Message", "Formateur non trouvé");
            }
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> modifierFormateur(String id, String email, String nomPrenom, String motDePasse, int numTelephone, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && "ADMIN".equals(authenticationFilter.RecupererRole(token))) {
            Query query = new Query(Criteria.where("id").is(id));
            Formateur formateur = mongoTemplate.findOne(query, Formateur.class);
            if (formateur != null) {
                String passEncode = authenticationFilter.encrypt(motDePasse);
                Update update = new Update()
                        .set("email", email)
                        .set("nomPrenom", nomPrenom)
                        .set("motDePasse", passEncode)
                        .set("numTelephone", numTelephone);
                mongoTemplate.updateFirst(query, update, Formateur.class);
                map.put("Message", "Compte mis à jour avec succès");
            } else {
                map.put("Message", "Formateur non trouvé");
            }
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> ActiverCompteFormateur(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(where("_id").is(id));
            Update update = new Update().set("active", true);
            mongoTemplate.updateFirst(query, update, Formateur.class);
            map.put("Message", "Le compte a été activé");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> DesactiverCompteFormateur(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(where("_id").is(id));
            Update update = new Update().set("active", false);
            mongoTemplate.updateFirst(query, update, Formateur.class);
            map.put("Message", "Le compte a été désactivé");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
