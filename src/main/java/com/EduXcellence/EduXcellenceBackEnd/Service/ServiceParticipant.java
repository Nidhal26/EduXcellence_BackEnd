package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.ParticipantRepo;
import com.EduXcellence.EduXcellenceBackEnd.Repository.PayementRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ServiceParticipant {
    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private PayementRepo payementRepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    AuthenticationFilter authenticationFilter;
    private boolean p;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String KEY = "T25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNlT25lX1BpZWNl";

    private String token;


    Map map = new HashMap();

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> loginParticipant(Participant participant) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(participant.getEmail(), "i"));
        Participant user = mongoTemplate.findOne(query, Participant.class);
        if (user == null || !bCryptPasswordEncoder.matches(participant.getMotDePasse(), user.getMotDePasse())) {
            map.put("Message", "Invalid email or password");
            map.put("verif", "false");
        } else {
            token = Jwts.builder()
                    .setSubject(participant.getEmail())
                    .claim("id", user.getId())
                    .claim("NomPrenom", user.getNomPrenom())
                    .claim("NiveauDEtude", user.getNiveauDEtude())
                    .claim("Role", user.getRole())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                    .signWith(SignatureAlgorithm.HS256, KEY)
                    .compact();
            map.put("Message", "Bonjour " + user.getNomPrenom());
            map.put("verif", "true");
            map.put("Token", token);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> inscriptionParticipant(Participant participant) {
        Query query = new Query();
        Query existEmail = query(Criteria.where("email").regex(participant.getEmail(), "i"));
        Long part = mongoTemplate.count(existEmail, Participant.class);
        if (part == 0) {
            String encryptedPassword = bCryptPasswordEncoder.encode(participant.getMotDePasse());
            participant.setMotDePasse(encryptedPassword);
            this.participantRepo.save(participant);
            map.put("Message", "Ajouté avec Succées");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("Message", "Email existe Deja");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerParticipants(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("tableParticipant", this.participantRepo.findAll());
        } else {
            map.put("Message", "Acceé réfuse");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<Map> listerParticipantsVerifier(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query();
            Long NbrParticipantVerifier = mongoTemplate.count(query.addCriteria(Criteria.where("verification").is("true")), Participant.class);
            Long NbrParticipantNonVerifier = mongoTemplate.count(query.addCriteria(Criteria.where("verification").is("false")), Participant.class);
            map.put("ParticipantVerifier", NbrParticipantVerifier);
            map.put("ParticipantNonVerifier", NbrParticipantNonVerifier);
        } else {
            map.put("Message", "Acceé refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerNombreDeParticipants(String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("NombreDeParticipants", this.participantRepo.count());
        } else {
            map.put("Message", "Accés refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> insererBonDeCommande(String token, MultipartFile bonDeCommande,
                                                    String ParticipantID,String FormationID) throws IOException {
        Query query = new Query();
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("USER")) {
            Payement payement = mongoTemplate.findOne(query(Criteria.where("ParticipantID").is(ParticipantID).and("FormationID").is(FormationID)), Payement.class);
            payement.setBonDeCommande(saveFile(bonDeCommande));
            payementRepo.save(payement);
            map.put("Message", "Votre bon de commande a été inséré");
        } else {
            map.put("Message", "Accés refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = String.valueOf(new Random().nextInt(1000000)) + "_" + file.getOriginalFilename();
        Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            Files.createDirectories(fileStorageLocation);
        }
        Files.copy(file.getInputStream(), fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<UrlResource> recupererBonDeCommande(String token, String ParticipantID, String FormationID) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("USER")) {
            Payement payement = mongoTemplate.findOne(query(Criteria.where("ParticipantID").is(ParticipantID).and("FormationID").is(FormationID)), Payement.class);

            if (payement != null && payement.getBonDeCommande() != null) {
                Path filePath = Paths.get("uploads").toAbsolutePath().resolve(payement.getBonDeCommande()).normalize();
                try {
                    UrlResource resource = new UrlResource(filePath.toUri());
                    if (resource.exists() && resource.isReadable()) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                        return ResponseEntity.ok()
                                .headers(headers)
                                .body(resource);
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                } catch (MalformedURLException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerUnSeulParticipant(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("Participant", this.participantRepo.findById(id));
        } else {
            map.put("Message", "Accés refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> InscriptionAuFormation(Payement payement, String Token) throws IOException {
        if (authenticationFilter.VerifierTOKEN(Token) && authenticationFilter.RecupererRole(token).equals("USER")) {
            payementRepo.save(payement);
            map.put("Message", "Votre inscription est enregistrée avec succès, Veuillez attendre la vérification de l'administrateur");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> modifierParticipant(String id, String email, String nomPrenom, String motDePasse, String niveauDEtude, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            String PassEnco = bCryptPasswordEncoder.encode(motDePasse);
            Query query = new Query(Criteria.where("id").is(id));
            Update updateemail = new Update().set("email", email);
            Update updatenomPrenom = new Update().set("nomPrenom", nomPrenom);
            Update updateniveauDEtude = new Update().set("niveauDEtude", niveauDEtude);
            Update updatemotDePasse = new Update().set("motDePasse", PassEnco);
            mongoTemplate.updateFirst(query, updateemail, Participant.class);
            mongoTemplate.updateFirst(query, updatenomPrenom, Participant.class);
            mongoTemplate.updateFirst(query, updatemotDePasse, Participant.class);
            mongoTemplate.updateFirst(query, updatemotDePasse, Participant.class);
            mongoTemplate.updateFirst(query, updateniveauDEtude, Participant.class);
            map.put("Message", "Mise a Jour avec succeé");
        } else {
            map.put("Message", "acceé refuser");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> verifierCompteParticipant(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("verification", true);
            mongoTemplate.updateFirst(query, update, Participant.class);
            map.put("Message", "compte verifier");
        } else {
            map.put("Message", "Acceé refuser");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}

