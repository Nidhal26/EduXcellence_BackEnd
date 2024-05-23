package com.EduXcellence.EduXcellenceBackEnd.Repository;

import com.EduXcellence.EduXcellenceBackEnd.Models.Affectation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectationRepo extends MongoRepository<Affectation, String> {
}
