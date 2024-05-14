package com.EduXcellence.EduXcellenceBackEnd.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
public class Formateur {

    @Id
    private String   id;
    private String email;
    private String motDePasse;
    private String nomPrenom;
    private int    numTelephone;
    private ArrayList<String>FormationId = new ArrayList<>();
    private boolean active=false;
    private String Role = "USER";

}
