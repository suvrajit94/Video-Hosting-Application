/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suvrajit.s3.services.S3Services;
import com.suvrajit.s3.services.impl.S3ServicesImpl;

/**
 *
 * @author I327917
 */
@SpringBootApplication
public class SpringS3AmazonApplication implements CommandLineRunner {
    
    @Autowired
    S3Services s3Services;

    @Value("${s3.uploadfile}")
    private String uploadFilePath;

    @Value("${s3.key}")
    private String downloadKey;

    public static void main(String[] args) {
        SpringApplication.run(SpringS3AmazonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------- START UPLOAD FILE ----------------");
        System.out.println(s3Services);
        s3Services.uploadFile("eula.1028.txt", uploadFilePath);
        System.out.println("---------------- START DOWNLOAD FILE ----------------");
        s3Services.downloadFile(downloadKey); 
    }
}
