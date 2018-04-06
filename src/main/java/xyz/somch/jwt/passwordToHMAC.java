/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.jwt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import org.jose4j.keys.HmacKey;

/**
 *
 * @author dark_
 */
public class passwordToHMAC {
    public static Key stringToHMAC(String secret) throws UnsupportedEncodingException{
        Key secretHMAC = new HmacKey(secret.getBytes("UTF-8"));
        return secretHMAC;
    }
}