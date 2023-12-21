package com.samsung.framework.common.config;

import com.samsung.framework.common.utils.PropertiesUtil;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Autowired
    PropertiesUtil propertiesUtil;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(propertiesUtil.getJasypt().getEncryptor().getPassword());
        config.setPoolSize("1");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);

        return encryptor;
    }

    /**
     * jasypt 암호화
     * @param input
     * @return
     */
    public String jasyptEncrypt(String input) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(propertiesUtil.getJasypt().getEncryptor().getPassword());

        return encryptor.encrypt(input);
    }

    /**
     * jasypt 복호화
     * @param input
     * @return
     */
    public String jasyptDecrypt(String input) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(propertiesUtil.getJasypt().getEncryptor().getPassword());

        return encryptor.decrypt(input);
    }
}
