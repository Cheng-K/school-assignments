package com.apcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

/*Description : PersonDetection is one of the subcomponents of the simulated system. It is a thread that performs
 * detection to ensure that no one is in the platform before closing exit gate and only one person is in the platform
 * before closing the entry gate. All operations are represented as a method in this class.
 * */
public class PersonDetection implements Runnable, Observable, Observer {
    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String nonePersonDetected = "NonePerson.PersonDetection";
    public static final String onePersonDetected = "OnePerson.PersonDetection";

    private int totalPerson = 0;

    public PersonDetection() {
        subscribers = new HashMap<>();
        subscribers.put(PersonDetection.nonePersonDetected, new ArrayList<>());
        subscribers.put(PersonDetection.onePersonDetected, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();
        // Initialize the events Responses
        // Event 1 : Open entry -> Validate one person came in
        eventsResponses.put(GateControl.openedEntry, () -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.enterPlatform();
                setTotalPerson(1);
                Response result = verifyOnlyOnePerson();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to verify only one person in platform");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(PersonDetection.onePersonDetected)) {
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 2 : Open exit -> Validate no person left
        eventsResponses.put(GateControl.openedExit, () -> {
            return (res) -> {
                Passenger currentPassenger = res.getCurrentPassenger();
                currentPassenger.exitPlatform();
                setTotalPerson(0);
                Response result = verifyNoPerson();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to verify no person left in platform");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(PersonDetection.nonePersonDetected)) {
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
            System.err.println("ERROR: ");
            e.printStackTrace();
        }
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyOnlyOnePerson() {
        return totalPerson == 1 ? Utility.createOkResponse(PersonDetection.onePersonDetected) : Utility.createFailedResponse(PersonDetection.onePersonDetected, new Exception("No person / Too many person detected."));
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyNoPerson() {
        return totalPerson == 0 ? Utility.createOkResponse(PersonDetection.nonePersonDetected) : Utility.createFailedResponse(PersonDetection.nonePersonDetected, new Exception("Person detected."));
    }

    public void setTotalPerson(int person) {
        totalPerson = person;
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
        return "PersonDetection";
    }
}