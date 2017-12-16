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
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 *
 * @author I327917
 */
@SpringBootApplication
public class SpringS3AmazonApplication{
//    SpringBootServletInitializer
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(SpringS3AmazonApplication .class);
//    }
    
    public static void main(String[] args) {
        SpringApplication.run(SpringS3AmazonApplication.class, args);
    }

}
