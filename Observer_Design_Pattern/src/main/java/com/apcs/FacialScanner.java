package com.apcs;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

class FacialScanner implements Runnable, Observable, Observer {

    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String getFacial = "GetFacial.FacialScanner";

    public FacialScanner() {
        subscribers = new HashMap<>();
        subscribers.put(FacialScanner.getFacial, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();
        // Initialize the events Responses
        // Event 1 : Close entry gate -> start scanning
        eventsResponses.put(GateControl.closedEntry, () -> {
            return (res) -> {
                System.out.println("APCS : Scanning your face... Please look into the camera in front of you.");
                Passenger currentPassenger = res.getCurrentPassenger();
                Response result = getFacialData(currentPassenger);
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to get passenger's facial data");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(FacialScanner.getFacial)) {
                    subscriber.sendEvent(result);
                }
            };
        });
    }

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

    public Response getFacialData(Passenger passenger) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Utility.createOkResponse(FacialScanner.getFacial, passenger.getFacialData().getBytes(StandardCharsets.UTF_8));
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
                throw new RuntimeException("Message is not acknowledged by " + this + " after waiting for 1 second");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "FacialScanner";
    }
}
