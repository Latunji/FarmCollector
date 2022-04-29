package com.interswitch.bifrost.cardservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class CardCryptoUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private static SecretKeySpec getAESSecretKey(String issuerSecret) {
        return new SecretKeySpec(issuerSecret.getBytes(), "AES");
    }

    private static AlgorithmParameterSpec getIvParamSpec() {
        byte[] initVector = new byte[16];
        Arrays.fill(initVector, (byte) 0x0);
        AlgorithmParameterSpec parameterSpec = new IvParameterSpec(initVector);
        return parameterSpec;
    }

    public static String encryptData(String data, String issuerSecret) throws InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, getAESSecretKey(issuerSecret), getIvParamSpec());
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return new String(Base64.encode(encryptedData));
    }

    public static String decryptData(String encryptedData, String issuerSecret) throws InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, getAESSecretKey(issuerSecret), getIvParamSpec());
        byte[] decodedData = Base64.decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, "UTF-8");
    }
}
