import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

public class Passport {

    private final byte[] data;
    private final byte[] issuer;

    public Passport (String name) {
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
