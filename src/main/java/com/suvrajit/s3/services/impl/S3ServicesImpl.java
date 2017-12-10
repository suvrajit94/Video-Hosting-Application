/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.services.impl;

import com.suvrajit.s3.services.*;
import com.suvrajit.s3.config.S3Config;
import com.suvrajit.s3.util.Utility;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.suvrajit.s3.Entity.UploadObj;
import com.suvrajit.s3.transcoder.jobs.TranscoderJobCreationService;

/**
 *
 * @author I327917
 */
@Service
public class S3ServicesImpl implements S3Services {

    private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private TranscoderJobCreationService transcoderJobCreationService;

    @Value("${s3.bucket}")
    private String bucketName;
    
    @Value("${s3.bucket.output}")
    private String outputBucketName;

    @Override
    public void downloadFile(String keyName) {
        try {

            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
            Utility.displayText(s3object.getObjectContent());
            logger.info("===================== Import File - Done! =====================");
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            logger.info("IOE Error Message: " + ioe.getMessage());
        }
    }

    @Override
    public void uploadFile(UploadObj uploadObj) {
        try {
            String uploadFilePath = uploadObj.getUploadFilePath();
            File file = new File(uploadFilePath);
            logger.info("FilePath: " + uploadFilePath);
            logger.info("Bucket name: " + bucketName);
            logger.info("Client name: " + s3client);
            s3client.putObject(new PutObjectRequest(bucketName, uploadObj.getKey(), file));
            logger.info("===================== Upload File - Done! =====================");

        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }

    }

    @Override
    public void deleteFile(String keyName) {
        try {

            System.out.println("Deleting an object");
            logger.info("bucket name: " + bucketName);
            logger.info("key: " + keyName);
            s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            logger.info("===================== Deletion File - Done! =====================");
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
    }

    @Override
    public S3Object viewFile(String keyName) {
        try {

            System.out.println("Viewing an object");
            return s3client.getObject(new GetObjectRequest(bucketName, keyName));
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
        return null;
    }

    @Override
    public S3Object viewFile(String keyName, String encoding) {
        try {
            System.out.println("Viewing an object");
            CreateJobResult createJobResult = transcoderJobCreationService.createJob(keyName, encoding);
            logger.info("Successfully created job: " + createJobResult.getJob().getId());
            logger.info("Output Bucket Name: " + outputBucketName);
            logger.info("key name: " + keyName + "_transcoded");
            return s3client.getObject(new GetObjectRequest(outputBucketName, keyName + "_transcoded"));
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
        return null;
    }

}
