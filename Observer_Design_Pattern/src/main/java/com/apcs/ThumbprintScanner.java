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

public class ThumbprintScanner implements Runnable,Observable, Observer {

    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String getThumbprint = "GetThumbprint.ThumbprintScanner";

    public ThumbprintScanner() {
        subscribers = new HashMap<>();
        subscribers.put(ThumbprintScanner.getThumbprint,new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();

        // Initialize the events Responses
        // Event 1 : Close entry gate -> start scanning
        eventsResponses.put(GateControl.closedEntry,() -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.scanThumb();
                System.out.println("APCS : Scanning your thumbprint... Please do not remove your thumb from the scanner.");
                Response result = getThumbprintData(currentPassenger);
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to get passenger's thumbprint data");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(ThumbprintScanner.getThumbprint)){
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response getThumbprintData(Passenger passenger) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(toString(),passenger.getThumbprintData().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void addObserver(String event, Observer observer) {
        if (! subscribers.containsKey(event))
            throw new IllegalArgumentException(observer + " trying to subscribe to an unknown event of " + this);
        subscribers.get(event).add(observer);
    }

    @Override
    public void sendEvent(Response res) {
        try {
            if (!eventsReceived.tryTransfer(res, 1, TimeUnit.SECONDS)) {
                throw new RuntimeException("Message is not acknowledged by " + eventsReceived + " after waiting for 1 second");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "ThumbprintScanner";
    }
}
