package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Administrateur;
import com.EduXcellence.EduXcellenceBackEnd.Models.Attestation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Formateur;
import com.EduXcellence.EduXcellenceBackEnd.Service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/apiAdmin")
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

    @PostMapping("/ajouterAdmin")
    public void ajouterAdministrateur(@RequestBody Administrateur administrateur){
        this.serviceadministrateur.ajouterAdmin(administrateur);
    }

    /*-------------------------------Gestion des formateurs---------------------------------*/
    @PostMapping("/ajouterFormateur")
    public String NouvelleFormateur (@RequestHeader("Token") String Token,@ModelAttribute Formateur F){
       return serviceFormateur.ajouterFormateur(F,Token);
    }

    @GetMapping("/listerFormateurs")
    public List<Formateur> RecupererFormateurs(@RequestHeader("Token") String Token){
        return serviceFormateur.listerFormateurs(Token);
    }

    /*------------------------------Gestion des formations------------------------------------*/

    @SneakyThrows
    @PostMapping("/NouvelleFormation")
    public String ajouterformation (@ModelAttribute Formation formation, @RequestHeader String Token)  {
        return serviceFormation.ajouterFormation(formation,Token);

    }

    @GetMapping("/listerFormations")
    public List<Formation> listerFormations(@RequestHeader("Token") String Token){
        return serviceFormation.listerFormations(Token);
    }

    /*-------------------------------Gestion des attestations----------------------------------*/

    @PostMapping("/ajouterAttestation")
    public void ajouterAttestation(@RequestHeader("Token") String Token,@RequestBody Attestation attestation){
        serviceAttestation.ajouterAttestation(attestation,Token);
    }

    @GetMapping("/listerAttestations")
    public List<Attestation> listerAttestations(@RequestHeader("Token") String Token){
        return serviceAttestation.listerAttestations(Token);
    }

    @PutMapping("/verificationCompte")
    public String verificationCompte(@Param("id") String id,@RequestHeader("Token") String token){
        return serviceadministrateur.verifierCompteParticipant(id, token);
    }

    @PutMapping("/ActiverCompteFormateur")
    public String ActiverCompteFormateur(@Param("id") String id,@RequestHeader("Token") String token) {
        return serviceadministrateur.ActiverCompteFormateur(id, token);
    }
    @PutMapping("/DesactiverCompteFormateur")
    public String DesactiverCompteFormateur(@Param("id") String id,@RequestHeader("Token") String token) {
        return serviceadministrateur.DesactiverCompteFormateur(id, token);
    }
    @PutMapping("/modifierCompteParticipant")
    public String modifierParticipant(@Param("id") String id,
                                    @RequestParam("email") String email,
                                    @RequestParam("motDePasse") String motDePasse,
                                    @RequestParam String nomPrenom ,
                                    @RequestHeader("Token") String token,
                                      @RequestParam("niveauDEtude") String niveauDEtude) {
        return serviceadministrateur.modifierParticipant(id,email,nomPrenom,motDePasse,niveauDEtude,token);
    }
    @PutMapping("/modifierCompteFormateur")
    public String modifierformateur(@Param("id") String id,
                                      @RequestParam("email") String email,
                                      @RequestParam("motDePasse") String motDePasse,
                                      @RequestParam String nomPrenom ,
                                      @RequestHeader("Token") String token,
                                      @RequestParam("numTelephone") int numTelephone) {
        return serviceadministrateur.modifierFormateur(id,email,nomPrenom,motDePasse,numTelephone,token);
    }

}
