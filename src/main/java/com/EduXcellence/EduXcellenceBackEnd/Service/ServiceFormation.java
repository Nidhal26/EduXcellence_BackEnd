package com.EduXcellence.EduXcellenceBackEnd.Service;


import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceFormation {

    @Autowired
    private FormationRepo formationRepo;

    @Autowired
    AuthenticationFilter authFiltre;

    @Autowired
    MongoTemplate mongoTemplate;

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
                map.put("Message", "La formation a été ajoutée avec succès.");
            }
        } else {
            map.put("Message", "Accès non autorisé.");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> ActiverFormation(String id, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", true);
            mongoTemplate.updateFirst(query, update, Formation.class);
            map.put("Message", "Formation Activer");
        } else {
            map.put("Message", "Acceé refuser");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> DesactiverFormation(String id, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("affiche", false);
            mongoTemplate.updateFirst(query, update, Formation.class);
            map.put("Message", "Formation Desactiver");
        } else {
            map.put("Message", "Acceé refuser");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> modifierFormation(String id, Formation formation, String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("id").is(id));
            Update updatethemeFormation = new Update().set("themeFormation", formation.getThemeFormation());
            Update updatedesciption = new Update().set("desciption", formation.getDesciption());
            Update updatedatedebut = new Update().set("datedebut", formation.getDatedebut());
            Update updatedatefin = new Update().set("datefin", formation.getDatefin());
            Update updateprix = new Update().set("prix", formation.getPrix());
            mongoTemplate.updateFirst(query, updatethemeFormation, Formation.class);
            mongoTemplate.updateFirst(query, updatedesciption, Formation.class);
            mongoTemplate.updateFirst(query, updatedatedebut, Formation.class);
            mongoTemplate.updateFirst(query, updatedatefin, Formation.class);
            mongoTemplate.updateFirst(query, updateprix, Formation.class);
            map.put("Message", "Mise a Jour avec succeé");
        } else {
            map.put("Message", "acceé refuser");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public ResponseEntity<Map> listerFormations(String token) {
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            map.put("tableFormation", this.formationRepo.findAll());
        } else {
            map.put("Message", "Acceé refuseé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
