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

import java.util.Date;
import java.util.List;

@Service
public class ServiceAttestation {
    @Autowired
    private AttestationRepo attestationRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private FormationRepo formationRepo;


    /*-------------------------------Gestion des attestations----------------------------------*/

    public void ajouterAttestation(Attestation attestation, String token) {
        AuthenticationFilter authFiltre = new AuthenticationFilter();
        if (authFiltre.VerifierTOKEN(token)) {
            this.attestationRepo.save(attestation);
        }
    }

    public List<Attestation> listerAttestations(String token) {
        AuthenticationFilter authFiltre = new AuthenticationFilter();
        if (authFiltre.VerifierTOKEN(token)) {
            return this.attestationRepo.findAll();
        } else {
            return null;
        }
    }

    public Attestation generateAttestation(String participantId, String courseId) {
        Participant participant = participantRepo.findById(participantId).orElse(null);
        Formation course = formationRepo.findById(courseId).orElse(null);
        if (participant != null && course != null) {
            Attestation attestation = new Attestation();
            attestation.setNomParticipant(participant.getNomPrenom());
            attestation.setNomParticipant(course.getThemeFormation());
            attestation.setDate(new Date());
            // Additional logic to generate attestation
            return attestation;
        }
        return null;
    }
}
