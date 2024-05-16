package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Attestation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.PayementRepo;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceAttestation;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceParticipant;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServicePayement;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/apiParticipant")
public class ParticipantController {
    @Autowired
    private ServiceParticipant serviceParticipant;
    @Autowired
    private ServicePayement servicePayement;
    @Autowired
    private ServiceAttestation serviceAttestation;
    @Autowired
    private PayementRepo payementRepo;

    /*-------------------------------Gestion des participants---------------------------*/

    @GetMapping("/listerParticipants")
    public ResponseEntity<Map> listerParticipants(String token) {
        return this.serviceParticipant.listerParticipants(token);
    }


    /*-----------------------------------Gestions des payements---------------------------*/
    @SneakyThrows
    @PostMapping("/InscriptionAuFormation")
    public ResponseEntity<Map> InscriptionAuFormation(@ModelAttribute Payement payement,
                                                      @RequestHeader("Token") String token) throws IOException {
        return serviceParticipant.InscriptionAuFormation(payement, token);
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

    @GetMapping("/listerParticipantsVerifier")
    public ResponseEntity<Map> listerParticipantsVerifier(@RequestHeader String token) {
        return serviceParticipant.listerParticipantsVerifier(token);
    }

    @PostMapping("/insererBonDeCommande")
    public ResponseEntity<Map> insererBonDeCommande(@RequestHeader String token,@RequestParam MultipartFile bonDeCommande, @RequestParam String ParticipantID, @RequestParam String FormationID) throws IOException {
        return serviceParticipant.insererBonDeCommande(token, bonDeCommande,ParticipantID,FormationID);
    }

    @GetMapping("/recupererBonDeCommande")
    public ResponseEntity<UrlResource> recupererBonDeCommande(@RequestHeader String token, @RequestParam String ParticipantID, @RequestParam String FormationID){
        return serviceParticipant.recupererBonDeCommande(token, ParticipantID,FormationID);
    }

}