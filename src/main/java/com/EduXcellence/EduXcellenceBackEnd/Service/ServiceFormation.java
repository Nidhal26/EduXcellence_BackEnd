package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Repository.FormationRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ServiceFormation {

    @Autowired
    private FormationRepo formationRepo;

    @Autowired
    AuthenticationFilter authFiltre;

    @Autowired
    MongoTemplate mongoTemplate;

    /*------------------------------Gestion des formations------------------------------------*/

    public String ajouterFormation(Formation formation, String token) {
        AuthenticationFilter authFilter = new AuthenticationFilter();
        if (authFilter.VerifierTOKEN(token)) {
            String msg = null;
            Query query = new Query(Criteria.where("datedebut").is(formation.getDatedebut()));
            Long exist = mongoTemplate.count(query,Formation.class);
            if (exist > 0) {
                return "Il ya une formation affecter a cette date";
            } else if (formation.getDatefin().before(formation.getDatedebut())){
                return "Date de fin formation avant Date de debut";
            }else{
            formationRepo.save(formation);
            msg = "La formation a été ajoutée avec succès.";
            return msg;
        } }else {
            return "Accès non autorisé.";
        }
    }


    public List<Formation> listerFormations(String token) {
        AuthenticationFilter authFiltre = new AuthenticationFilter();
        if (authFiltre.VerifierTOKEN(token)) {
            return this.formationRepo.findAll();
        } else {
            return null;
        }
    }


}
