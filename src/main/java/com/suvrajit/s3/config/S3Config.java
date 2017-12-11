package com.suvrajit.s3.config;

/**
 * Created by I327917 on 12/10/2017.
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.util.HashMap;

@Configuration
public class S3Config {

    @Value("${aws.access_key_id}")
    private String awsId;

    @Value("${aws.secret_access_key}")
    private String awsKey;

    @Value("${s3.region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        return s3Client;
    }
    
    @Bean
    public HashMap<String,String> presetMap() {
       HashMap<String, String> presetMap = new HashMap<String, String>();
       presetMap.put("360p", "1351620000001-000050");
       presetMap.put("480p", "1351620000001-000020");
       presetMap.put("720p", "1351620000001-000010");
       presetMap.put("1080p", "1351620000001-000001");
       return presetMap;
    }
}
