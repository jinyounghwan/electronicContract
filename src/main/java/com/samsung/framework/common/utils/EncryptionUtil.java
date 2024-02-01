package com.samsung.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {

    @Value("${encryption.salt}")
    private String salt;

    /**
     * 단방향 암호화
     * @param source
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String encrypt(String source) throws NoSuchAlgorithmException {

//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//        byte[] bytes = new byte[128];
//        random.nextBytes(bytes);
//        String salt = new String(Base64.getEncoder().encode(bytes));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(this.salt.getBytes());
        md.update(source.getBytes());
        String encrypted = String.format("%0128x", new BigInteger(1, md.digest()));

        return encrypted;
    }

}
