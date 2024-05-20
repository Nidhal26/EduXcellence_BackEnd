package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Evaluation;
import com.EduXcellence.EduXcellenceBackEnd.Repository.EvaluationRepo;
import com.EduXcellence.EduXcellenceBackEnd.Service.ServiceFormation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiFormateur")
@CrossOrigin(origins = "*")
public class FormateurController {
    @Autowired
    private EvaluationRepo evaluationRepo;
    @Autowired
    private ServiceFormation serviceFormation;

    /*------------------------------Gestion des évaluations--------------------------------*/
    @PostMapping("/ajouterEvaluation")
    public void ajouterEvaluation(@RequestBody Evaluation evaluation) {
        this.evaluationRepo.save(evaluation);
    }

    @GetMapping("/listerEvaluations")
    public List<Evaluation> listerEvaluations() {
        return this.evaluationRepo.findAll();
    }
}
