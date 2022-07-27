// TODO : Implement Encryption and Decryption using SecretKey and CipherSpi
// TODO : Implement Signing using Signed object java
public class Utility {
    public static Response createOkResponse () {
        return new Response(Response.STATUS.OK);
    }
    public static Response createFailedResponse (Exception e) {
        return new Response(Response.STATUS.FAILED,e);
    }

//    public static byte[] encryptData () {}
//    public static byte[] decryptData () {}
}

class Response {
    enum STATUS {OK, FAILED};
    private STATUS status;
    private Exception errorObj;

    public Response (Response.STATUS value, Exception obj) {
        status = value;
        errorObj = obj;
    }

    public Response(Response.STATUS value) {
        status = value;
        errorObj = null;
    }
    public STATUS getStatus() {
        return status;
    }

    public Exception getErrorObj() {
        return errorObj;
    }
}

class Security {}