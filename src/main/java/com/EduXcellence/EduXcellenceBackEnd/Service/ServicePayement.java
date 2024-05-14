package com.EduXcellence.EduXcellenceBackEnd.Service;

import com.EduXcellence.EduXcellenceBackEnd.Models.Payement;
import com.EduXcellence.EduXcellenceBackEnd.Repository.PayementRepo;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ServicePayement {
    @Autowired
    private PayementRepo payementRepo;

}
