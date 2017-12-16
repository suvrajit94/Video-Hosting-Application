/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.notification;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author I327917
 */

@Service
public class JobStatusNotificationService {

    public AmazonSQSClient getAmazonSQSAsyncClient() {
        return amazonSQSAsyncClient;
    }
    
    @Value("${aws.access_key_id}")
    private String access_key;

    @Value("${aws.secret_access_key}")
    private String secret_key;

    @Value("${s3.region}")
    private String region;
    
    private AmazonSQSClient amazonSQSAsyncClient;
    
    private SqsQueueNotificationWorker sqsQueueNotificationWorker;
    
    
    public JobStatusNotificationService setAmazonSQSClient(){
        this.amazonSQSAsyncClient =  (AmazonSQSClient) AmazonSQSAsyncClient.builder().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key))).withRegion(region).build();
        return this;
    }
    
//    public JobStatusNotificationService setSqsQueueNotificationWorker(){
//        this.sqsQueueNotificationWorker = new SqsQueueNotificationWorker(this.amazonSQSAsyncClient, SQS_QUEUE_URL);
//        return this;
//    }
    
    
    
    
   
    /**
     * Waits for the specified job to complete by adding a handler to the SQS
     * notification worker that is polling for status updates.  This method
     * will block until the specified job completes.
     * @param jobId
     * @param sqsQueueNotificationWorker
     * @throws InterruptedException
     */
    public void waitForJobToComplete(final String jobId, SqsQueueNotificationWorker sqsQueueNotificationWorker) throws InterruptedException {
        
        // Create a handler that will wait for this specific job to complete.
        JobStatusNotificationHandler handler = new JobStatusNotificationHandler() {
            
            @Override
            public void handle(JobStatusNotification jobStatusNotification) {
                if (jobStatusNotification.getJobId().equals(jobId)) {
                    System.out.println(jobStatusNotification);
                    
                    if (jobStatusNotification.getState().isTerminalState()) {
                        synchronized(this) {
                            this.notifyAll();
                        }
                    }
                }
            }
        };
        sqsQueueNotificationWorker.addHandler(handler);
        
        // Wait for job to complete.
        synchronized(handler) {
            handler.wait();
        }
        
        // When job completes, shutdown the sqs notification worker.
        sqsQueueNotificationWorker.shutdown();
    }
}
