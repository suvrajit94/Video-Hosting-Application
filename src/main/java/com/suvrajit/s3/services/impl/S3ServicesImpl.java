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
import com.amazonaws.services.elastictranscoder.model.Job;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.suvrajit.s3.Entity.UploadObj;
import com.suvrajit.s3.notification.JobStatusNotificationService;
import com.suvrajit.s3.notification.SqsQueueNotificationWorker;
import com.suvrajit.s3.transcoder.jobs.TranscoderJobCreationService;
import com.suvrajit.s3.util.KeyGenerator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

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
    private JobStatusNotificationService jobStatusNotificationService;

    @Autowired
    private TranscoderJobCreationService transcoderJobCreationService;

    @Value("${s3.bucket}")
    private String bucketName;
    
    @Value("${sqs.url}")
    private String SQS_QUEUE_URL;

    @Value("${s3.bucket.output}")
    private String outputBucketName;

    @Override
    public void downloadFile(String keyName, HttpServletResponse response) {
        try {

            response.setContentType("video/mp4");

            PrintWriter out = response.getWriter();
            try {
                out = response.getWriter();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(S3ServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            String filename = keyName + ".mp4";

            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            InputStream inputStream  = s3client.getObject(new GetObjectRequest(bucketName, keyName)).getObjectContent();

            int i;
            while ((i = inputStream.read()) != -1) {
                out.write(i);
            }
            //fileInputStream.close();
            out.close();

            System.out.println("Downloading an object");

            // System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
            //Utility.displayText(s3object.getObjectContent());
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
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(S3ServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
//        } catch (IOException ioe) {
//            logger.info("IOE Error Message: " + ioe.getMessage());
//        }

//        } catch (IOException ioe) {
//            logger.info("IOE Error Message: " + ioe.getMessage());
//        }
    }

    @Override
    public void uploadFile(MultipartFile video) {
        try {
            logger.info("Bucket name: " + bucketName);
            logger.info("Client name: " + s3client);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(video.getContentType());
            metadata.setContentLength(video.getSize());

            System.out.println(video.getName());
            System.out.println(video.getOriginalFilename());

            s3client.putObject(new PutObjectRequest(bucketName, video.getOriginalFilename(), video.getInputStream(), metadata));
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
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(S3ServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("key: " + keyName);
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
            System.out.println(createJobResult.getJob().getArn());
            logger.info("Successfully created job: " + createJobResult.getJob().getId());
            logger.info("Output Bucket Name: " + outputBucketName);
            String newKey = KeyGenerator.parseKeyName(keyName, encoding);
            System.out.println(keyName);
            System.out.println(newKey);
            logger.info("key name: " + newKey);
            
            SqsQueueNotificationWorker sqsQueueNotificationWorker = new SqsQueueNotificationWorker(jobStatusNotificationService.setAmazonSQSClient().getAmazonSQSAsyncClient(), SQS_QUEUE_URL);
            Thread notificationThread = new Thread(sqsQueueNotificationWorker);
            notificationThread.start();
            
            jobStatusNotificationService.waitForJobToComplete(createJobResult.getJob().getId(), sqsQueueNotificationWorker);
            
            logger.info("============ JOB COMPLETED =============================");
            return s3client.getObject(new GetObjectRequest(outputBucketName, newKey));
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
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(S3ServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
