package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Attestation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceAttestation;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceParticipant;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServicePayement;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/apiParticipant")
public class ParticipantController {
    @Autowired
    private ServiceParticipant serviceParticipant;
    @Autowired
    private ServicePayement servicePayement;
    @Autowired
    private ServiceAttestation serviceAttestation;

    /*-------------------------------Gestion des participants---------------------------*/

    @GetMapping("/listerParticipants")
    public List<Participant> listerParticipants(){
        return this.serviceParticipant.listerParticipants();
    }



    /*-----------------------------------Gestions des payements---------------------------*/
    @SneakyThrows
    @PostMapping("/Inscription")
    public String InscritAuCours(@RequestParam String nomPrenomParticipant,
                                @RequestParam double prix,
                                @RequestAttribute MultipartFile bonDeCommande,
                                 @RequestHeader("Token") String token) throws IOException {
        return serviceParticipant.InscriptionAuCours(nomPrenomParticipant,bonDeCommande,prix,token);
    }



    @GetMapping("/generate")
    public ResponseEntity<Attestation> generateAttestation(@RequestParam String participantId, @RequestParam String courseId) {
        Attestation attestation = serviceAttestation.generateAttestation(participantId, courseId);
        if (attestation != null) {
            // Generate PDF or return attestation data as JSON
            return ResponseEntity.ok(attestation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}