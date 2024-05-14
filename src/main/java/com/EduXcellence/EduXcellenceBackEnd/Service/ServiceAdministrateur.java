package com.EduXcellence.EduXcellenceBackEnd.Service;


import com.EduXcellence.EduXcellenceBackEnd.Models.Administrateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Repository.AdministrateurRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class ServiceAdministrateur {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    AuthenticationFilter authenticationFilter;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AdministrateurRepo administrateurRepo;

    public void ajouterAdmin(Administrateur administrateur){
        this.administrateurRepo.save(administrateur);
    }

    public String verifierCompteParticipant(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("verification", true);
            mongoTemplate.updateFirst(query, update, Participant.class);
            return "compte verifier";
        } else {
            return "Acceé refuser";
        }
    }

    public String ActiverCompteFormateur(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("active", true);
            mongoTemplate.updateFirst(query, update, Formateur.class);
            return "compte Activer";
        } else {
            return "Acceé refuser";
        }
    }
    public String DesactiverCompteFormateur(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("active", false);
            mongoTemplate.updateFirst(query, update, Formateur.class);
            return "compte Désactivé";
        } else {
            return "Accés refusé";
        }
    }

    public String modifierParticipant(String id, String email,String nomPrenom,String motDePasse,String niveauDEtude,String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            String PassEnco = bCryptPasswordEncoder.encode(motDePasse);
            Query query = new Query(Criteria.where("id").is(id));
            Update updateemail = new Update().set("email", email);
            Update updatenomPrenom = new Update().set("nomPrenom", nomPrenom);
            Update updateniveauDEtude = new Update().set("niveauDEtude", niveauDEtude);
            Update updatemotDePasse = new Update().set("motDePasse", PassEnco);
            mongoTemplate.updateFirst(query, updateemail, Participant.class);
            mongoTemplate.updateFirst(query, updatenomPrenom, Participant.class);
            mongoTemplate.updateFirst(query, updatemotDePasse, Participant.class);
            mongoTemplate.updateFirst(query, updateniveauDEtude, Participant.class);
            return "Mise à Jour avec succés";
        } else {
            return "Accés refusé";
        }
    }

    public String modifierFormateur(String id, String email,String nomPrenom,String motDePasse,int numTelephone,String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            String PassEnco = bCryptPasswordEncoder.encode(motDePasse);
            Query query = new Query(Criteria.where("id").is(id));
            Update updateemail = new Update().set("email", email);
            Update updatenomPrenom = new Update().set("nomPrenom", nomPrenom);
            Update updatenumTelephone = new Update().set("numTelephone", numTelephone);
            Update updatemotDePasse = new Update().set("motDePasse", PassEnco);
            mongoTemplate.updateFirst(query, updateemail, Formateur.class);
            mongoTemplate.updateFirst(query, updatenomPrenom, Formateur.class);
            mongoTemplate.updateFirst(query, updatemotDePasse, Formateur.class);
            mongoTemplate.updateFirst(query, updatenumTelephone, Formateur.class);
            return "Mise a Jour avec succeé";
        } else {
            return "acceé refuser";
        }
    }

    public String ActiverFormation(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", true);
            mongoTemplate.updateFirst(query, update, Formation.class);
            return "Formation Activée";
        } else {
            return "Accés refusé";
        }
    }
    public String DesactiverCompteFormation(String id, String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", false);
            mongoTemplate.updateFirst(query, update, Formation.class);
            return "Formation Désactivée";
        } else {
            return "Accés refusé";
        }
    }

    public String modifierFormation(String id, Formation formation,String token) {
        if (authenticationFilter.VerifierTOKEN(token)) {
            Query query = new Query(Criteria.where("id").is(id));
            Update updatethemeFormation = new Update().set("themeFormation",formation.getThemeFormation() );
            Update updatedesciption = new Update().set("desciption", formation.getDesciption());
            Update updatedatedebut = new Update().set("datedebut", formation.getDatedebut());
            Update updatedatefin = new Update().set("datefin", formation.getDatefin());
            Update updateprix = new Update().set("prix", formation.getPrix());
            mongoTemplate.updateFirst(query, updatethemeFormation, Formation.class);
            mongoTemplate.updateFirst(query, updatedesciption, Formation.class);
            mongoTemplate.updateFirst(query, updatedatedebut, Formation.class);
            mongoTemplate.updateFirst(query, updatedatefin, Formation.class);
            mongoTemplate.updateFirst(query, updateprix, Formation.class);
            return "Mise a Jour avec succés";
        } else {
            return "accés refusé";
        }
    }



}


