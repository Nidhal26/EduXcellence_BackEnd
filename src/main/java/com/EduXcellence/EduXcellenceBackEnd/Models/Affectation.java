package com.EduXcellence.EduXcellenceBackEnd.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
public class Affectation {
    private boolean Disponiblité;
    private Date datedebut;
    private Date datefin;
    private Formation formation;
    private Formateur formateur;
}
