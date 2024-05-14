package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.ParticipantRepo;
import com.EduXcellence.EduXcellenceBackEnd.Repository.PayementRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    /*-------------------------------Gestion des participants---------------------------*/

    public List<Participant> listerParticipants(){
        return this.participantRepo.findAll();
    }

    public Optional<Participant> listerUnSeulParticipant(String id){
        return this.participantRepo.findById(id);
    }

    public String InscriptionAuCours (String nomPrenomParticipant, MultipartFile bonDeCommande, double prix, String Token) throws IOException {
        if (authenticationFilter.VerifierTOKEN(Token)){
            Payement payement = new Payement(nomPrenomParticipant,bonDeCommande,prix);
            payementRepo.save(payement);
            return "Votre Inscrit est Enregisté avec Succés";
        }else{
            return "Accés refusé";
        }
    }

}

