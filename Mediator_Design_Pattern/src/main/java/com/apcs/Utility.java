package com.apcs;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/* Description : The Utility class contains utility methods that provide abstraction to the underlying logic */
public class Utility {
    public static Response createOkResponse() {
        return new Response(Response.STATUS.OK, new byte[]{});
    }

    public static Response createOkResponse(byte[] data) {
        return new Response(Response.STATUS.OK, data);
    }

    public static Response createFailedResponse(Exception e) {
        return new Response(Response.STATUS.FAILED, e);
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

/* Description : Response instances are used in conjunction with operations performed by each thread. It is used to
 * transmit the details of the operations to the receiver. This includes :
 * status -- to indicate whether the operation is successful or failed
 * data -- some operation might produce data that is necessary for receiver
 * errorObj -- operation that failed might attach an errorObj so that receiver can determine the source of error
 * currentPassenger -- to indicate the passenger processed
 * */
class Response {
    enum STATUS {
        OK, FAILED
    }

    private final STATUS status;
    private final Exception errorObj;
    private final byte[] data;

    public Response(Response.STATUS value, Exception obj) {
        status = value;
        errorObj = obj;
        data = null;
    }

    public Response(Response.STATUS value, byte[] data) {
        status = value;
        errorObj = null;
        this.data = data;
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
}

/* Description : This class contains all the logic in creating a signature engine that can sign and verify signature of
 *  signed objects. This class also contains all the logic in creating ciphers that can decrypt and encrypt data.
 * */
class Security {
    private static Cipher encryptionCipher;
    private static Cipher decryptionCipher;
    private static final SecretKey key;
    private static final String encryptDecryptAlg;

    private static final KeyPair signatureKeys;

    private static Signature signatureEngine;

    // Initialization block for encryption and decryption algorithms and a secret key for the cipher
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

    // Initialization block for the signature algorithm and keys for signing and verification.
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