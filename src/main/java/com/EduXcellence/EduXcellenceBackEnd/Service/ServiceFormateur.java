package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Repository.FormateurRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

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

    /*-------------------------------Gestion des formateurs---------------------------------*/

    public String ajouterFormateur(Formateur F, String token) {
        AuthenticationFilter authFiltre = new AuthenticationFilter();
        if (authFiltre.VerifierTOKEN(token)&&authFiltre.RecupererRole(token)=="ADMIN") {
            Query query = new Query();
            Query existEmail = query(Criteria.where("email").is(F.getEmail()));
            Long part = mongoTemplate.count(existEmail, Formateur.class);
            if (part == 0) {
                String motdepasseCrypter = bCryptPasswordEncoder.encode(F.getMotDePasse());
                F.setMotDePasse(motdepasseCrypter);
                formateurRepo.save(F);
                return "Formateur ajouté Avec Succés";
            } else {
                return "Cet Email Exite Deja";
            }
        } else {
            return "Accès refusé";
        }
    }

    public List<Formateur> listerFormateurs(String token) {
        AuthenticationFilter authFiltre = new AuthenticationFilter();
        if (authFiltre.VerifierTOKEN(token)) {
            return this.formateurRepo.findAll();
        } else {
            return null;
        }
    }


}
