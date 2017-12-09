/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.services.impl;

import com.suvrajit.s3.services.*;
import com.suvrajit.config.S3Config;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 *
 * @author I327917
 */
public class S3ServicesImpl implements S3Services {

    private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Override
    public void downloadFile(String keyName) {

    }

    @Override
    public void uploadFile(String keyName, String uploadFilePath) {

    }

}
