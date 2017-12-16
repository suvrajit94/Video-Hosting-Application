/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.notification;

/**
 *
 * @author I327917
 */
public interface JobStatusNotificationHandler {
    public void handle(JobStatusNotification jobStatusNotification);
}
