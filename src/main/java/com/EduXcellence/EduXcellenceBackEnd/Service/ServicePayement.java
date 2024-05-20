package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Formation;
import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.PayementRepo;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicePayement {
    @Autowired
    private PayementRepo payementRepo;
    @Autowired
    private AuthenticationFilter authFiltre;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Payement payement0 = new Payement();
    Map map = new HashMap();
    /*-----------------------------------Gestions des payements---------------------------*/

    public ResponseEntity<Map> listerLesPayements(String token, String idparticipant) {
        Map map1 = new HashMap();
        if (authFiltre.VerifierTOKEN(token) && authFiltre.RecupererRole(token).equals("ADMIN")) {
            Query query = new Query(Criteria.where("ParticipantID").is(idparticipant));
            List<Payement> payement = mongoTemplate.find(query, Payement.class);
            List<Map> list = new ArrayList<Map>();
            for (Payement pay : payement) {
                Query query2 = new Query(Criteria.where("idformation").is(pay.getFormationID()));
                Formation formation = mongoTemplate.findOne(query2, Formation.class);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("idpayement", pay.getPayementId());
                map2.put("themedeformation", formation.getThemeFormation());
                map2.put("prix", formation.getPrix());
                map2.put("date", payement0.formatDate(pay.getDate()));
                map2.put("etatdepaiement", pay.isVerifierInscription());
                map2.put("bondecommande", pay.getBonDeCommande());
                list.add(map2);
                System.out.println(map2);
            }
            map1.put("TablePayement", list);
            System.out.println(map1);
        } else {
            map1.put("Message", "Accès refusé");
        }
        return new ResponseEntity<>(map1, HttpStatus.OK);
    }

    public byte[] getFile(String fileName) throws IOException {
        Path filePath = Paths.get("uploads").resolve(fileName).normalize();
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }


}
