package com.abdulkarimalbaik.dev.photogram.Model;

public class DecryptImage {

    private byte[] imageEncrypt;
    private int imageEncryptLength;

    public DecryptImage(byte[] imageEncrypt, int imageEncryptLength) {
        this.imageEncrypt = imageEncrypt;
        this.imageEncryptLength = imageEncryptLength;
    }

    public byte[] getImageEncrypt() {
        return imageEncrypt;
    }

    public void setImageEncrypt(byte[] imageEncrypt) {
        this.imageEncrypt = imageEncrypt;
    }

    public int getImageEncryptLength() {
        return imageEncryptLength;
    }

    public void setImageEncryptLength(int imageEncryptLength) {
        this.imageEncryptLength = imageEncryptLength;
    }
}
