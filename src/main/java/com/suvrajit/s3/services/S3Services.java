/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.services;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.suvrajit.s3.Entity.UploadObj;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author I327917
 */
public interface S3Services {
    public void downloadFile(String keyName, HttpServletResponse response);
    public void uploadFile(MultipartFile file);
    public void deleteFile(String keyName);
    public  S3Object viewFile(String keyName);
    public  S3Object viewFile (String keyName, String encoding);
}
