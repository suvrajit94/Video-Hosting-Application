/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.suvrajit.s3.Entity.UploadObj;
import com.suvrajit.s3.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author I327917
 */
@RestController
public class VideoHostController {
    @Autowired
    S3Services s3Services;
    
    @RequestMapping (method = RequestMethod.GET, value = "/videos/{key}")
    public S3ObjectInputStream getVideo(@PathVariable String key){
        System.out.println(" ============ URL Reached ============");
        return s3Services.viewFile(key).getObjectContent();
    }
    
    @RequestMapping (method = RequestMethod.GET, value = "/videos/{key}/{encoding}")
    public S3ObjectInputStream getVideo(@PathVariable String key, @PathVariable String encoding){
        System.out.println(" ============ URL Reached ============");
        return s3Services.viewFile(key,encoding).getObjectContent();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/videos")
    public void uploadVideo(@RequestBody UploadObj uploadObj) {
        System.out.println(" ============ URL Reached ============");
        s3Services.uploadFile(uploadObj);
    }
    
    @RequestMapping (method = RequestMethod.DELETE, value = "/videos/{key}")
    public void deleteVideo(@PathVariable String key){
        System.out.println(" ============ URL Reached ============");
        s3Services.deleteFile(key);
    }
    
    @RequestMapping (method = RequestMethod.GET, value = "/videos/{key}/download")
    public void downloadVideo(@PathVariable String key){
        System.out.println(" ============ URL Reached ============");
        s3Services.downloadFile(key);
    }
}
