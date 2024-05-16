package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Attestation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/apiAdmin")
@CrossOrigin(origins = "*")
public class AdministrateurController {

    @Autowired
    private ServiceAdministrateur serviceadministrateur;
    @Autowired
    private ServiceFormateur serviceFormateur;
    @Autowired
    private ServiceFormation serviceFormation;
    @Autowired
    private ServiceAttestation serviceAttestation;
    @Autowired
    private ServiceParticipant serviceParticipant;

    /*-------------------------------Gestion des formateurs---------------------------------*/
    @PostMapping("/AjouterFormateur")
    public ResponseEntity<Map> NouvelleFormateur(@RequestHeader("Token") String Token, @ModelAttribute Formateur F) {
        return serviceFormateur.AjouterFormateur(F, Token);
    }

    @GetMapping("/listerFormateurs")
    public ResponseEntity<Map> RecupererFormateurs(@RequestHeader("Token") String Token) {
        return serviceFormateur.listerFormateurs(Token);
    }

    /*------------------------------Gestion des formations------------------------------------*/

    @SneakyThrows
    @PostMapping("/AjouterFormation")
    public ResponseEntity<Map> ajouterformation(@ModelAttribute Formation formation, @RequestHeader("Token") String Token) {
        return serviceFormation.ajouterFormation(formation, Token);
    }

    @GetMapping("/listerFormations")
    public ResponseEntity<Map> listerFormations(@RequestHeader("Token") String Token) {
        return serviceFormation.listerFormations(Token);
    }

    /*-------------------------------Gestion des attestations----------------------------------*/

    @PostMapping("/ajouterAttestation")
    public void ajouterAttestation(@RequestHeader("Token") String Token, @RequestBody Attestation attestation) {
        serviceAttestation.ajouterAttestation(attestation, Token);
    }

    @GetMapping("/listerAttestations")
    public ResponseEntity<Map> listerAttestations(@RequestHeader("Token") String Token) {
        return serviceAttestation.listerAttestations(Token);
    }

    @PutMapping("/verificationCompteParticipant")
    public ResponseEntity<Map> verificationCompte(@Param("id") String id, @RequestHeader("Token") String token) {
        return serviceParticipant.verifierCompteParticipant(id, token);
    }

    @PutMapping("/ActiverCompteFormateur")
    public ResponseEntity<Map> ActiverCompteFormateur(@Param("id") String id, @RequestHeader("Token") String token) {
        return serviceFormateur.ActiverCompteFormateur(id, token);
    }

    @PutMapping("/DesactiverCompteFormateur")
    public ResponseEntity<Map> DesactiverCompteFormateur(@Param("id") String id, @RequestHeader("Token") String token) {
        return serviceFormateur.DesactiverCompteFormateur(id, token);
    }

    @PutMapping("/modifierCompteParticipant")
    public ResponseEntity<Map> modifierParticipant(@Param("id") String id,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("motDePasse") String motDePasse,
                                                   @RequestParam String nomPrenom,
                                                   @RequestHeader("Token") String token,
                                                   @RequestParam("niveauDEtude") String niveauDEtude) {
        return serviceParticipant.modifierParticipant(id, email, nomPrenom, motDePasse, niveauDEtude, token);
    }

    @PutMapping("/modifierCompteFormateur")
    public ResponseEntity<Map> modifierformateur(@Param("id") String id,
                                    @RequestParam("email") String email,
                                    @RequestParam("motDePasse") String motDePasse,
                                    @RequestParam String nomPrenom,
                                    @RequestHeader("Token") String token,
                                    @RequestParam("numTelephone") int numTelephone) {
        return serviceFormateur.modifierFormateur(id, email, nomPrenom, motDePasse, numTelephone, token);
    }

    @PutMapping("/VerifierInscription")
    public ResponseEntity<Map> VerifierInscription(@RequestParam String formationId) {
        return serviceadministrateur.VerifierInscription(formationId);
    }


}
