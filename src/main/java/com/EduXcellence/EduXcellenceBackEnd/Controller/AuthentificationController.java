package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Administrateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Participant;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceAuthentification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ApiAuth")
@CrossOrigin(origins = "*")
public class AuthentificationController {

    @Autowired
    private ServiceAuthentification serviceAuthentification;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@ModelAttribute Participant participant) {
        String tokenResponse = serviceAuthentification.login(participant);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Token", tokenResponse);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/inscriptionParticipant")
    public ResponseEntity<Map<String, String>> inscriptionParticipant(@ModelAttribute Participant participant) {
        return serviceAuthentification.inscriptionParticipant(participant);
    }

    @PostMapping("/VerifierAuthentification")
    public boolean VerifierAuthentification(@RequestParam String token){
        return this.serviceAuthentification.isAuthenticated(token);
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<Map<String, String>> loginAdmin(@ModelAttribute Administrateur administrateur) {
        return serviceAuthentification.loginAdmin(administrateur);
    }

    @PostMapping("/loginFormateur")
    public ResponseEntity<Map<String, String>> loginFormateur(@ModelAttribute Formateur formateur) {
        return serviceAuthentification.loginFormateur(formateur);
    }
}
