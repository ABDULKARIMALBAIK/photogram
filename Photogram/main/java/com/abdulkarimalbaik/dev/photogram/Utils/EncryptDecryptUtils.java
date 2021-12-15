package com.abdulkarimalbaik.dev.photogram.Utils;

import com.abdulkarimalbaik.dev.photogram.Model.DecryptImage;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecryptUtils {

    ///////////////variables
    private byte[] keyBytes = "...........".getBytes();
    private byte[] ivBytes = "..........".getBytes();
    private SecretKeySpec key = new SecretKeySpec(keyBytes  , "DES");
    private IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
    private Cipher cipher =  Cipher.getInstance("DES/CTR/NoPadding" , "BC");
    private byte[] cipherText;
    private int ctLength;

    public EncryptDecryptUtils() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    }
    ///////////////


    public byte[] decryptImage(byte[] encryptImage , int encrypt_image_length) {

        try {

            cipher.init(Cipher.DECRYPT_MODE , key , ivParameterSpec);

            byte[] decryptImage = new byte[cipher.getOutputSize(encrypt_image_length)];
            int ptLength = cipher.update(encryptImage , 0 , encrypt_image_length , decryptImage , 0);
            ptLength += cipher.doFinal(decryptImage , ptLength);

            return decryptImage;
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public DecryptImage encryptImage(byte[] normalImage){

        try {

            cipher.init(Cipher.ENCRYPT_MODE , key , ivParameterSpec);
            byte[] encryptImage = new byte[cipher.getOutputSize(normalImage.length)];

            int encrypt_image_length = cipher.update(normalImage , 0 , normalImage.length , encryptImage , 0);
            encrypt_image_length += cipher.doFinal(encryptImage , encrypt_image_length);

            return new DecryptImage(encryptImage , encrypt_image_length);
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }
}
