/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suvrajit.s3.util;

/**
 *
 * @author I327917
 */
public class KeyGenerator {
    public static String parseKeyName(String key, String encoding){
        String[] parts = key.split("\\.");
        System.out.println(parts);
        StringBuffer sb = new StringBuffer();
        
        for (int i=0; i < parts.length - 1; i++){
            sb.append(parts[i]);
        }
        
        String format = parts[parts.length - 1];
        
        sb.append("transcoded" + encoding + "." +   format);
        
        return sb.toString();
    }
}
