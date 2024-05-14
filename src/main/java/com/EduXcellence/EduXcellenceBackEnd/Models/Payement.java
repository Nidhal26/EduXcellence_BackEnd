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
import java.util.Date;
import java.util.Random;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
public class Payement  {

    @Id
    private String payementId;
    private String nomPrenomParticipant;
    private String bonDeCommande;
    private boolean payee = false;
    private Date date = new Date();
    private double Prix;

    public Payement(String nomPrenomParticipant,  MultipartFile bonDeCommande,double prix) throws IOException {
        this.nomPrenomParticipant = nomPrenomParticipant;
        this.bonDeCommande = saveFile(bonDeCommande);
        this.date = new Date();
        this.Prix = prix;
        payee = false;
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
