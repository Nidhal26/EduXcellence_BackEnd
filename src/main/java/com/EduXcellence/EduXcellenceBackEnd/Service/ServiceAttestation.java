package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Attestation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Repository.AttestationRepo;
import com.EduXcellence.EduXcellenceBackEnd.Repository.FormationRepo;
import com.EduXcellence.EduXcellenceBackEnd.Repository.ParticipantRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceAttestation {

    @Autowired
    private AttestationRepo attestationRepo;
    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private FormationRepo formationRepo;
    @Autowired
    AuthenticationFilter authenticationFilter;


    /*-------------------------------Gestion des attestations----------------------------------*/

    public ResponseEntity<Map> ajouterAttestation(Attestation attestation, String token) {
        Map map = new HashMap();
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            this.attestationRepo.save(attestation);
            map.put("Message", "Attestation Ajoutée Avec Succés");
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<Map> listerAttestations(String token) {
        Map map = new HashMap();
        if (authenticationFilter.VerifierTOKEN(token) && authenticationFilter.RecupererRole(token).equals("ADMIN")) {
            map.put("tableAttestation", this.attestationRepo.findAll());
        } else {
            map.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public Attestation generateAttestation(String participantId, String courseId) {
        Participant participant = participantRepo.findById(participantId).orElse(null);
        Formation course = formationRepo.findById(courseId).orElse(null);
        if (participant != null && course != null) {
            Attestation attestation = new Attestation();
            attestation.setDate(new Date());
            // Additional logic to generate attestation
            return attestation;
        }
        return null;
    }
}
