package com.apcs;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;


public class Utility {
    public static Response createOkResponse(String content){
        return new Response(content,Response.STATUS.OK, new byte[]{});
    }

    public static Response createOkResponse(String content,byte[] data) {
        return new Response(content,Response.STATUS.OK, data);
    }

    public static Response createFailedResponse(String content,Exception e) {
        return new Response(content,Response.STATUS.FAILED, e);
    }

    public static byte[] encryptData(byte[] string) throws IllegalBlockSizeException, BadPaddingException {
        return Security.getEncryptionCipher().doFinal(string);
    }

    public static byte[] decryptData(byte[] string) throws IllegalBlockSizeException, BadPaddingException {
        return Security.getDecryptionCipher().doFinal(string);
    }

    public static byte[] signObject(byte[] object) throws SignatureException {
        Security.getSignatureSignEngine().update(object);
        return Security.getSignatureSignEngine().sign();
    }

    public static boolean verifyObject(byte[] object) throws SignatureException {
        return Security.getSignatureVerificationEngine().verify(object);
    }
}

class Response {
    enum STATUS {
        OK, FAILED
    }

    private String sender;
    private String receiver;
    private String content;

    private Passenger currentPassenger;
    private final STATUS status;
    private final Exception errorObj;
    private final byte[] data;

    public Response(String content, Response.STATUS value, Exception obj) {
        this.content = content;
        this.sender = null;
        this.receiver = null;
        status = value;
        errorObj = obj;
        data = null;
    }

    public Response(String content, Response.STATUS value, byte[] data) {
        this.content = content;
        this.sender = null;
        this.receiver = null;
        status = value;
        errorObj = null;
        this.data = data;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver (String receiver){
        this.receiver = receiver;
    }

    public byte[] getData() {
        return data;
    }
    public STATUS getStatus() {
        return status;
    }
    public Exception getErrorObj() {
        return errorObj;
    }
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setCurrentPassenger (Passenger p) {
        if (currentPassenger != null)
            throw new RuntimeException(this + " response already has an assigned passenger. Modification is not allowed");
        currentPassenger = p;
    }
    public Passenger getCurrentPassenger () {
        return currentPassenger;
    }

}

class Security {
    private static Cipher encryptionCipher;
    private static Cipher decryptionCipher;
    private static final SecretKey key;
    private static final String encryptDecryptAlg;

    private static final KeyPair signatureKeys;

    private static Signature signatureEngine;

    static {
        encryptDecryptAlg = "DES";
        String secretKey = "RTS-Assignment-2205-055620";
        SecretKeyFactory keyFactory;
        try {
            keyFactory = SecretKeyFactory.getInstance(encryptDecryptAlg);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            key = keyFactory.generateSecret(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), encryptDecryptAlg));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        String signatureAlg = "DSA";
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(signatureAlg);
            keyPairGenerator.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        signatureKeys = keyPairGenerator.generateKeyPair();
    }

    public static Cipher getEncryptionCipher() {
        if (encryptionCipher == null) {
            try {
                encryptionCipher = Cipher.getInstance(encryptDecryptAlg);
                encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
        return encryptionCipher;
    }

    public static Cipher getDecryptionCipher() {
        if (decryptionCipher == null) {
            try {
                decryptionCipher = Cipher.getInstance(encryptDecryptAlg);
                decryptionCipher.init(Cipher.DECRYPT_MODE, key);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
        return decryptionCipher;
    }

    public static Signature getSignatureSignEngine() {
        if (signatureEngine == null) {
            try {
                signatureEngine = Signature.getInstance("SHA1withDSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            signatureEngine.initSign(signatureKeys.getPrivate());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return signatureEngine;
    }

    public static Signature getSignatureVerificationEngine() {
        if (signatureEngine == null) {
            try {
                signatureEngine = Signature.getInstance("SHA1withDSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            signatureEngine.initVerify(signatureKeys.getPublic());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return signatureEngine;
    }
}
