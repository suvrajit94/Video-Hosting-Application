/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.transcoder.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClientBuilder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 *
 * @author I327917
 */
@Component
public class AWSTrancoderClient {
    private AmazonElasticTranscoderClient amazonElasticTranscoderClient;

    @Value("${aws.access_key_id}")
    private String access_key;
    
    @Value("${aws.secret_access_key}")
    private String secret_key;
    
    @Value("${s3.region}")
    private String region;
    
    public AmazonElasticTranscoderClient getAmazonElasticTranscoderClient() {
        return amazonElasticTranscoderClient;
    }

    public void setAmazonElasticTranscoderClient() {
        BasicAWSCredentials creds = new BasicAWSCredentials(access_key, secret_key);
        this.amazonElasticTranscoderClient = (AmazonElasticTranscoderClient) AmazonElasticTranscoderClient.builder().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
    }

    
    
    
    
}
