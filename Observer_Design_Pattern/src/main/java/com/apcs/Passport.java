package com.apcs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

/*Description: Passport class will be instantiated as the passport object for each passenger. It contains two fields.
 * data field is an encrypted field of passenger's name
 * issuer field is a field with a value that is signed by a signature engine which can be verified
 * Both fields are important for the validation and verification process on passports.
 * */
public class Passport {

    private final byte[] data;
    private final byte[] issuer;

    public Passport(String name) {
        // Encrypt
        try {
            data = Utility.encryptData(name.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        // Signed
        try {
            issuer = Utility.signObject(name.getBytes(StandardCharsets.UTF_8));
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getIssuer() {
        return issuer;
    }


}
