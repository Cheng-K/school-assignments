package com.apcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

public class PersonDetection implements Runnable,Observable, Observer {
    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String nonePersonDetected = "NonePerson.PersonDetection";
    public static final String onePersonDetected = "OnePerson.PersonDetection";

    private int totalPerson = 0;

    public PersonDetection() {
        subscribers = new HashMap<>();
        subscribers.put(PersonDetection.nonePersonDetected,new ArrayList<>());
        subscribers.put(PersonDetection.onePersonDetected,new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();
        // Initialize the events Responses
        // Event 1 : Open entry -> Validate one person came in
        eventsResponses.put(GateControl.openedEntry,() -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.enterPlatform();
                setTotalPerson(1);
                Response result = verifyOnlyOnePerson();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to verify only one person in platform");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(PersonDetection.onePersonDetected)){
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 2 : Open exit -> Validate no person left
        eventsResponses.put(GateControl.openedExit,() -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.exitPlatform();
                setTotalPerson(0);
                Response result = verifyNoPerson();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to verify no person left in platform");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(PersonDetection.nonePersonDetected)){
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

    public Response verifyOnlyOnePerson() {
        return totalPerson == 1 ? Utility.createOkResponse(toString()) : Utility.createFailedResponse(toString(), new Exception("No person / Too many person detected."));
    }

    public Response verifyNoPerson() {
        return totalPerson == 0 ? Utility.createOkResponse(toString()) : Utility.createFailedResponse(toString(), new Exception("Person detected."));
    }

    public void setTotalPerson(int person) {
        totalPerson = person;
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
        return "PersonDetection";
    }
}