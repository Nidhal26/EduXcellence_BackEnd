package com.EduXcellence.EduXcellenceBackEnd.Controller;

import com.EduXcellence.EduXcellenceBackEnd.Models.Evaluation;
import com.EduXcellence.EduXcellenceBackEnd.Repository.EvaluationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiFormateur")
@CrossOrigin(origins = "*")
public class FormateurController {
    @Autowired
    private EvaluationRepo evaluationRepo;

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
