/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.Entity;

/**
 *
 * @author I327917
 */
public class UploadObj {
    
    String key;
    String uploadFilePath;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }
    
    @Override
    public String toString(){
        return "Key: " + key + " uploadFilePath: " + uploadFilePath;
    }
}
