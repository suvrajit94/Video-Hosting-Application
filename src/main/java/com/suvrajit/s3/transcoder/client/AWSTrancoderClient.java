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
import org.springframework.stereotype.Service;


/**
 *
 * @author I327917
 */
@Service
public class AWSTrancoderClient {
    private AmazonElasticTranscoderClient amazonElasticTranscoderClient;

    public AmazonElasticTranscoderClient getAmazonElasticTranscoderClient() {
        return amazonElasticTranscoderClient;
    }

    public void setAmazonElasticTranscoderClient() {
        BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIDHI3QUZWQJB5F3A", "kKAzs3Lrey6bGCIQrZ57SOlNBJ5R+ud+kzq1teU6");
        this.amazonElasticTranscoderClient = (AmazonElasticTranscoderClient) AmazonElasticTranscoderClient.builder().withRegion("ap-south-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
//        this.amazonElasticTranscoderClient.
    }

    
    
    
    
}
