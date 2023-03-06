package com.apcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

/*Description : PassportScanner is one of the subcomponents of the simulated system. It is a thread that performs
 * passport scanning operations. All operations are represented as a method in this class.
 * */
public class PassportScanner implements Runnable, Observable, Observer {
    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String getIssuer = "GetIssuer.PassportScanner";
    public static final String getData = "GetData.PassportScanner";


    public PassportScanner() {
        subscribers = new HashMap<>();
        subscribers.put(PassportScanner.getData, new ArrayList<>());
        subscribers.put(PassportScanner.getIssuer, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();

        // Initialize the events Responses
        // Event 1 : Append new passenger -> Scan passport issuer
        eventsResponses.put(AutomatedPassportControlSystem.addedNewPassenger, () -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.scanPassport();
                System.out.println("APCS : Scanning your passport... Please do not remove your passport from the scanner.");
                Response result = getPassportIssuer(currentPassenger.getPassport());
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Passport data issuer cannot be collected.");
                result.setCurrentPassenger(currentPassenger);
                System.out.println("APCS : Done. You can remove your passport from the scanner. Validating your passport...");
                for (Observer subscriber : subscribers.get(PassportScanner.getIssuer)) {
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 2 : Close entry gate -> start scanning
        eventsResponses.put(GateControl.closedEntry, () -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.scanPassport();
                System.out.println("APCS : Scanning your passport... Please do not remove your passport from the scanner.");
                Response result = getPassportData(currentPassenger.getPassport());
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to get passenger's passport data");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(PassportScanner.getData)) {
                    subscriber.sendEvent(result);
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
            if (responseToEvent == null)
                throw new RuntimeException(this + " received an event that does not know how to handle..");
            responseToEvent.call().accept(receivedEvent);
        } catch (InterruptedException e) {
            System.err.println("WARNING : " + this + " has been interrupted and terminated.");
        } catch (Throwable e) {
            System.err.println("ERROR : ");
            e.printStackTrace();
        }
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response getPassportData(Passport passport) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Utility.createOkResponse(PassportScanner.getData, passport.getData());
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response getPassportIssuer(Passport passport) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(PassportScanner.getIssuer, passport.getIssuer());
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
        return "PassportScanner";
    }
}
