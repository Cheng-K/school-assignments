package com.apcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.function.Consumer;

/* Description : This class is one of the subcomponents of the simulated system. It is a thread that performs facial
 * scanning of passengers. All operations are represented as a method in this class. */

public class GateControl implements Runnable, Observable, Observer {

    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;

    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String openedExit = "OpenedExit.GateControl";
    public static final String openedEntry = "OpenedEntry.GateControl";
    public static final String closedExit = "ClosedExit.GateControl";
    public static final String closedEntry = "ClosedEntry.GateControl";

    public GateControl() {
        subscribers = new HashMap<>();
        subscribers.put(GateControl.openedEntry, new ArrayList<>());
        subscribers.put(GateControl.openedExit, new ArrayList<>());
        subscribers.put(GateControl.closedEntry, new ArrayList<>());
        subscribers.put(GateControl.closedExit, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();

        // Initialize the events responses
        // Event 1 : Validated issuer -> Open entry gate
        eventsResponses.put(DataProcessing.validatedPassport, () -> {
            return (res) -> {
                Response result = openEntry();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to open entry gate");
                result.setCurrentPassenger(res.getCurrentPassenger());
                for (Observer subscriber : subscribers.get(GateControl.openedEntry)) {
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 2 : Verified one person -> Close entry gate
        eventsResponses.put(PersonDetection.onePersonDetected, () -> {
            return (res) -> {
                Response result = closeEntry();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to close entry gate");
                result.setCurrentPassenger(res.getCurrentPassenger());
                System.out.println("APCS : Entry gate closed.");
                System.out.println("APCS : Please place your passport onto the scanner while your thumb on thumbprint scanner and look into the camera in front of you.");
                for (Observer subscriber : subscribers.get(GateControl.closedEntry)) {
                    subscriber.sendEvent(result);
                }

            };
        });

        // Event 3 : Verified passenger -> Open exit gate
        eventsResponses.put(DataProcessing.verifiedPassenger, () -> {
            return (res) -> {
                Response result = openExit();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to open exit gate");
                result.setCurrentPassenger(res.getCurrentPassenger());
                System.out.println("APCS : Exit gate opened.");
                for (Observer subscriber : subscribers.get(GateControl.openedExit)) {
                    subscriber.sendEvent(result);
                }
            };
        });

        // Event 4 : No person left -> Close exit gate
        eventsResponses.put(PersonDetection.nonePersonDetected, () -> {
            return (res) -> {
                Response result = closeExit();
                if (result.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to close exit gate");
                result.setCurrentPassenger(res.getCurrentPassenger());
                System.out.println("APCS : Exit gate closed.");
                for (Observer subscriber : subscribers.get(GateControl.closedExit)) {
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
            System.err.print("ERROR :");
            e.printStackTrace();
        }
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response openEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(GateControl.openedEntry);
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response closeEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(GateControl.closedEntry);
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response openExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(GateControl.openedExit);
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response closeExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(GateControl.closedExit);
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
        return "GateControl";
    }
}
