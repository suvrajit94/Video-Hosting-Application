/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.suvrajit.s3.Entity.UploadObj;
import com.suvrajit.s3.services.S3Services;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author I327917
 */
@RestController
public class VideoHostController {
    @Autowired
    S3Services s3Services;
    
    @RequestMapping (method = RequestMethod.GET, 
                     value = "/videos/{key:.+}")
    public void getVideo(@PathVariable String key, HttpServletResponse response){
        System.out.println(" ============ URL Reached ============");
        S3Object file = s3Services.viewFile(key);
        response.setContentType(file.getObjectMetadata().getContentType());
        try {
            IOUtils.copy(file.getObjectContent(), response.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(VideoHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping (method = RequestMethod.GET,
                     value = "/videos/{key:.+}/{encoding}")
    public void getVideo(@PathVariable String key, @PathVariable String encoding, HttpServletResponse response){
        System.out.println(" ============ URL Reached ============");
        S3Object file = s3Services.viewFile(key,encoding);
        response.setContentType(file.getObjectMetadata().getContentType());
        try {
            IOUtils.copy(file.getObjectContent(), response.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(VideoHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/videos",
                    consumes = {"multipart/form-data", "application/x-www-form-urlencoded"},
                    produces = "application/json; charset=UTF-8")
    public void uploadVideo(@RequestParam("file") MultipartFile video) {
        System.out.println(" ============ URL Reached ============");
        s3Services.uploadFile(video);
    }
    
    @RequestMapping (method = RequestMethod.DELETE, value = "/videos/{key:.+}")
    public void deleteVideo(@PathVariable String key){
        System.out.println(" ============ URL Reached ============");
        s3Services.deleteFile(key);
    }
    
    @RequestMapping (method = RequestMethod.GET, value = "/videos/{key}/download")
    public void downloadVideo(@PathVariable String key, HttpServletResponse response){
        System.out.println(" ============ URL Reached ============");
        s3Services.downloadFile(key, response);
    }
}
