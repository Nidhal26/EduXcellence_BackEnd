package com.EduXcellence.EduXcellenceBackEnd.Service;


import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.FormationRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ServiceFormation {

    @Autowired
    private FormationRepo formationRepo;

    @Autowired
    AuthenticationFilter authFiltre;

    @Autowired
    MongoTemplate mongoTemplate;
    Payement payement = new Payement();

    Map map = new HashMap();

    /*------------------------------Gestion des formations------------------------------------*/


    public ResponseEntity<Map> ajouterFormation(Formation formation, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("datedebut").is(formation.getDatedebut()));
            Long exist = mongoTemplate.count(query, Formation.class);
            if (exist > 0) {
                map.put("Message", "Il y a déjà une formation prévue à cette date");
            } else if (formation.getDatefin().before(formation.getDatedebut())) {
                map.put("Message", "La date de fin de la formation doit être après la date de début");
            } else if (formation.getDatedebut().before((new Date()))) {
                map.put("Message", "La date de début de la formation doit être aujourd'hui ou après");
            } else {
                formationRepo.save(formation);
                map.put("Message", "La formation a été ajoutée avec succès");
                map.put("verif", true);
            }
        } else {

        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> ActiverFormation(String id, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", true);
            mongoTemplate.updateFirst(query, update, Formation.class);
            map.put("Message", "Formation Activée");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> DesactiverFormation(String id, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", false);
            mongoTemplate.updateFirst(query, update, Formation.class);
            map.put("Message", "Formation Desactivée");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public ResponseEntity<Map> modifierFormation(String id, Formation formation, String token) {
        Map<String, String> map = new HashMap<>();

        if (authFiltre.VerifierTOKEN(token) && "ADMIN".equals(authFiltre.RecupererRole(token))) {

            Query query = new Query(Criteria.where("idformation").is(id));
            Update update = new Update()
                    .set("themeFormation", formation.getThemeFormation())
                    .set("desciption", formation.getDesciption())
                    .set("datedebut", formation.getDatedebut())
                    .set("datefin", formation.getDatefin())
                    .set("prix", formation.getPrix())
                    .set("participantID", formation.getParticipantID())
                    .set("formateurID", formation.getFormateurID())
                    .set("affiche", formation.isAffiche());
            System.out.println(update);
            mongoTemplate.updateFirst(query, update, Formation.class);
            map.put("Message", "Mise à jour avec succès");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("Message", "Accès refusé");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerFormations(String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            List<Formation> list = new ArrayList<>();
            map.put("TableFormation", this.formationRepo.findAll());
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<Map> listerUnSeulFormation(String token, String id) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Formation formation = mongoTemplate.findOne(query, Formation.class);
            map.put("Formation", formation);
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<Map> lesFormateursAffectesAFormation(String token, String formation) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query();
            query.addCriteria(Criteria.where("FormationID").is(formation));
            List<Formateur> list = mongoTemplate.find(query, Formateur.class);

            map.put("TableFormateurAF", list);
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
