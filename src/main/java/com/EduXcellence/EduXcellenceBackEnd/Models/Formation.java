package com.EduXcellence.EduXcellenceBackEnd.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
public class Formation {

    @Id
    private String idformation;
    private String themeFormation;
    private String desciption;
    private Date datedebut;
    private Date datefin;
    private double prix;
    private String ParticipantID;
    private String FormateurID;
    private boolean affiche = true;

    public Formation(String themeFormation, String desciption, Date datedebut, Date datefin, double prix) throws IOException {
        this.themeFormation = themeFormation;
        this.desciption = desciption;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.prix = prix;
        this.affiche = true;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = String.valueOf(new Random().nextInt(1000000)) + "_" + file.getOriginalFilename();
        Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            Files.createDirectories(fileStorageLocation);
        }
        Files.copy(file.getInputStream(), fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

}
