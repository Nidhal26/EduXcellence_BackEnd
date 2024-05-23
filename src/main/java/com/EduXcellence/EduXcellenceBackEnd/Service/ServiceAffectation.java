package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Affectation;
import com.EduXcellence.EduXcellenceBackEnd.Security.AuthenticationFilter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;

import javax.management.Query;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ServiceAffectation {

    @Autowired
    AuthenticationFilter authenticationFilter;
    @Autowired
    private MongoTemplate mongoTemplate;

//    public ResponseEntity<Map> PlanificationDeFormation(String token){
//        if (authenticationFilter.VerifierTOKEN(token)&&authenticationFilter.RecupererRole(token).equals("ADMIN")){
//            Query query = new Query();
//            query(Criteria.where("Disponiblité").is("true"));
//            List<Affectation> existeList = mongoTemplate.find()
//        }
//    }
}
