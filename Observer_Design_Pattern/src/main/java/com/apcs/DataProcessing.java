package com.apcs;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

/* Description : This class is one of the subcomponents of the simulated system. It is a thread that performs data
 *  processing such as passport validation, verification, and data uploading. All operations are represented as a
 *  method in this class. */

public class DataProcessing implements Runnable, Observable, Observer {
    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String verifiedPassenger = "Verified.DataProcessing";
    public static final String validatedPassport = "Validated.DataProcessing";

    private byte[] passportDataBuffer = null;
    private byte[] facialDataBuffer = null;
    private byte[] thumbprintDataBuffer = null;

    public DataProcessing() {
        subscribers = new HashMap<>();
        subscribers.put(DataProcessing.verifiedPassenger, new ArrayList<>());
        subscribers.put(DataProcessing.validatedPassport, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();

        // Initialize the events Responses
        // Event 1 : Get Passport Issuer -> Validate Passport Issuer
        eventsResponses.put(PassportScanner.getIssuer, () -> {
            return (res) -> {
                byte[] passportIssuer = res.getData();
                Response result = validatePassport(passportIssuer);
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to validate passport issuer.");
                result.setCurrentPassenger(res.getCurrentPassenger());
                System.out.println("APCS : Validated. Please step inside the platform after the gate opened");
                for (Observer subscriber : subscribers.get(DataProcessing.validatedPassport)) {
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 2 : Receive data from three scanners -> Verification
        eventsResponses.put(PassportScanner.getData, () -> {
            return (res) -> {
                passportDataBuffer = res.getData();
                if (facialDataBuffer != null && thumbprintDataBuffer != null) {
                    System.out.println("APCS : Done. You can remove your passport and thumb from the scanners.");
                    System.out.println("APCS : Verifying your credentials... ");
                    Response result1 = verifyPassportWithFacial(passportDataBuffer, facialDataBuffer);
                    Response result2 = verifyPassportWithThumbprint(passportDataBuffer, thumbprintDataBuffer);
                    if (result1.getStatus() == Response.STATUS.OK || result2.getStatus() == Response.STATUS.OK) {
                        System.out.println("APCS : Credentials successfully verified... Please wait for the system to upload your data.");
                        Response result3 = uploadData(res.getCurrentPassenger().getPassport());
                        if (result3.getStatus() != Response.STATUS.OK)
                            throw new RuntimeException("Unable to upload passenger data.");
                        System.out.println("APCS : Done. Please exit the platform once the exit gate is opened");
                        result3.setCurrentPassenger(res.getCurrentPassenger());
                        facialDataBuffer = null;
                        passportDataBuffer = null;
                        thumbprintDataBuffer = null;
                        for (Observer subscriber : subscribers.get(DataProcessing.verifiedPassenger)) {
                            subscriber.sendEvent(result3);
                        }
                    } else {
                        throw new RuntimeException("Unable to verify passenger passport");
                    }

                }
            };
        });

        eventsResponses.put(FacialScanner.getFacial, () -> {
            return (res) -> {
                facialDataBuffer = res.getData();
                if (passportDataBuffer != null && thumbprintDataBuffer != null) {
                    System.out.println("APCS : Done. You can remove your passport and thumb from the scanners.");
                    System.out.println("APCS : Verifying your credentials... ");
                    Response result1 = verifyPassportWithFacial(passportDataBuffer, facialDataBuffer);
                    Response result2 = verifyPassportWithThumbprint(passportDataBuffer, thumbprintDataBuffer);
                    if (result1.getStatus() == Response.STATUS.OK || result2.getStatus() == Response.STATUS.OK) {
                        System.out.println("APCS : Credentials successfully verified... Please wait for the system to upload your data.");
                        Response result3 = uploadData(res.getCurrentPassenger().getPassport());
                        if (result3.getStatus() != Response.STATUS.OK)
                            throw new RuntimeException("Unable to upload passenger data.");
                        System.out.println("APCS : Done. Please exit the platform once the exit gate is opened");
                        result3.setCurrentPassenger(res.getCurrentPassenger());
                        facialDataBuffer = null;
                        passportDataBuffer = null;
                        thumbprintDataBuffer = null;
                        for (Observer subscriber : subscribers.get(DataProcessing.verifiedPassenger)) {
                            subscriber.sendEvent(result3);
                        }
                    } else {
                        throw new RuntimeException("Unable to verify passenger passport");
                    }

                }
            };
        });
        eventsResponses.put(ThumbprintScanner.getThumbprint, () -> {
            return (res) -> {
                thumbprintDataBuffer = res.getData();
                if (passportDataBuffer != null && facialDataBuffer != null) {
                    System.out.println("APCS : Done. You can remove your passport and thumb from the scanners.");
                    System.out.println("APCS : Verifying your credentials... ");
                    Response result1 = verifyPassportWithFacial(passportDataBuffer, facialDataBuffer);
                    Response result2 = verifyPassportWithThumbprint(passportDataBuffer, thumbprintDataBuffer);
                    if (result1.getStatus() == Response.STATUS.OK || result2.getStatus() == Response.STATUS.OK) {
                        System.out.println("APCS : Credentials successfully verified... Please wait for the system to upload your data.");
                        Response result3 = uploadData(res.getCurrentPassenger().getPassport());
                        if (result3.getStatus() != Response.STATUS.OK)
                            throw new RuntimeException("Unable to upload passenger data.");
                        System.out.println("APCS : Done. Please exit the platform once the exit gate is opened");
                        result3.setCurrentPassenger(res.getCurrentPassenger());
                        facialDataBuffer = null;
                        passportDataBuffer = null;
                        thumbprintDataBuffer = null;
                        for (Observer subscriber : subscribers.get(DataProcessing.verifiedPassenger)) {
                            subscriber.sendEvent(result3);
                        }
                    } else {
                        throw new RuntimeException("Unable to verify passenger passport");
                    }

                }
            };
        });


    }

    /* Description : This run method will retrieve the responses received and fetch and execute the correct event
     *  response to the event. Event responses are stored in a consumer function wrapped in a callable. */
    @Override
    public void run() {
        // wait for events
        Response receivedEvent;
        try {
            receivedEvent = eventsReceived.take();
            Callable<Consumer<Response>> responseToEvent = eventsResponses.get(receivedEvent.getContent());
            if (responseToEvent == null) {
                System.err.println("ERROR : " + this + " received an event that does not know how to handle..");
                throw new RuntimeException(this + " received an event that does not know how to handle..");
            }
            responseToEvent.call().accept(receivedEvent);
        } catch (InterruptedException e) {
            System.err.println("WARNING : " + this + " has been interrupted and terminated.");
        } catch (Throwable e) {
            System.err.println("Error message ");
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
            return Utility.createFailedResponse(toString(), e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(DataProcessing.validatedPassport);

    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyPassportWithFacial(byte[] passport, byte[] facialData) {
        try {
            Thread.sleep(1000);
            byte[] decrypted = Utility.decryptData(passport);
            boolean match = Arrays.compare(decrypted, facialData) == 0;
            if (!match)
                return Utility.createFailedResponse(toString(), new Exception("Failed to match passport data with facial data"));
        } catch (IllegalBlockSizeException | BadPaddingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(DataProcessing.verifiedPassenger);
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyPassportWithThumbprint(byte[] passport, byte[] thumbprintData) {
        try {
            Thread.sleep(1000);
            byte[] decrypted = Utility.decryptData(passport);
            boolean match = Arrays.compare(decrypted, thumbprintData) == 0;
            if (!match)
                return Utility.createFailedResponse(toString(), new Exception("Failed to match passport data with thumbprint data"));
        } catch (IllegalBlockSizeException | BadPaddingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(DataProcessing.verifiedPassenger);
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response uploadData(Passport passport) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(DataProcessing.verifiedPassenger);
    }

    @Override
    public void addObserver(String event, Observer observer) {
        if (!subscribers.containsKey(event))
            throw new IllegalArgumentException(observer + " trying to subscribe to an unknown event of " + this);
        subscribers.get(event).add(observer);
    }

    @Override
    public void sendEvent(Response res) {
        try {
            if (!eventsReceived.tryTransfer(res, 60, TimeUnit.SECONDS)) {
                throw new RuntimeException("Message is not acknowledged by " + this + " after waiting for 60 second");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "DataProcessing";
    }
}

