package com.apcs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

/* Description : This class is one of the subcomponents of the simulated system. It is a thread that performs data
 *  processing such as passport validation, verification, and data uploading. All operations are represented as a
 *  method in this class. */

public class DataProcessing implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    public DataProcessing(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    /* Description : This run method will retrieve and execute the instructions received.
     *  Instructions are in the form of callable and the output from the instructions will be inserted into the
     *  resultOutput queue which will be acknowledged by the mediator thread. */
    @Override
    public void run() {
        try {
            Callable<Response> instructions;
            Response response;
            instructions = instructionsQueue.take();
            response = instructions.call();
            resultOutput.put(response);
        } catch (InterruptedException e) {
            System.err.println("WARNING : " + this + " has been interrupted and terminated.");
        } catch (Throwable e) {
            System.err.println("ERROR : ");
            e.printStackTrace();
        }
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response validatePassport(byte[] issuerString) {
        try {
            Thread.sleep(1000);
            boolean isLegit = Utility.verifyObject(issuerString);
            if (!isLegit) {
                throw new SignatureException("Unable to verify signature");
            }
        } catch (SignatureException e) {
            return Utility.createFailedResponse(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();

    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyPassportWithFacial(byte[] passport, byte[] facialData) {
        try {
            Thread.sleep(1000);
            byte[] decrypted = Utility.decryptData(passport);
            boolean match = Arrays.compare(decrypted, facialData) == 0;
            if (!match)
                return Utility.createFailedResponse(new Exception("Failed to match passport data with facial data"));
        } catch (IllegalBlockSizeException | BadPaddingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyPassportWithThumbprint(byte[] passport, byte[] thumbprintData) {
        try {
            Thread.sleep(1000);
            byte[] decrypted = Utility.decryptData(passport);
            boolean match = Arrays.compare(decrypted, thumbprintData) == 0;
            if (!match)
                return Utility.createFailedResponse(new Exception("Failed to match passport data with thumbprint data"));
        } catch (IllegalBlockSizeException | BadPaddingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response uploadData(Passport passport) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }
}
