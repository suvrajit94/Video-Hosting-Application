/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.transcoder.jobs;

import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.suvrajit.s3.transcoder.client.AWSTrancoderClient;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 *
 * @author I327917
 */
@Service
public class TranscoderJobCreationService {

    @Autowired
    private AWSTrancoderClient aWSTrancoderClient;

    @Value("${aws.transcoder.pipeline.id}")
    private String pipelineId;
    
    @Autowired
    private HashMap<String,String> presetMap;

    public CreateJobResult createJob(String keyName, String presetId) {
        try {
            aWSTrancoderClient.setAmazonElasticTranscoderClient();
            CreateJobRequest createJobRequest = new CreateJobRequest();
            JobInput jobInput = new JobInput();
            jobInput.withKey(keyName).withFrameRate("auto").withContainer("auto").setAspectRatio("auto");
            CreateJobOutput createJobOutput = new CreateJobOutput();
            createJobOutput.withKey(keyName + "-transcoded-" + presetId).setPresetId(presetMap.get(presetId));
            createJobRequest.withInput(jobInput).withOutput(createJobOutput).setPipelineId(pipelineId);
            System.out.println(aWSTrancoderClient.getAmazonElasticTranscoderClient());
            return aWSTrancoderClient.getAmazonElasticTranscoderClient().createJob(createJobRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
