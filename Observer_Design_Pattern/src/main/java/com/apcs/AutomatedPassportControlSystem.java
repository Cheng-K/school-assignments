package com.apcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/* Description : This class is the entry point of the simulated automated passport control system and should be
 *  instantiated to run this simulation. In the simulation, this class has a thread that emits an event to other threads
 *  whenever a passenger first use the system.*/

public class AutomatedPassportControlSystem implements Runnable, Observable, Observer {
    public final AtomicBoolean isRunning;
    public final ScheduledExecutorService threadPool;
    // Sub components
    private final GateControl gateControlThread;
    private final PassportScanner passportScannerThread;
    private final ThumbprintScanner thumbprintScannerThread;
    private final DataProcessing dataProcessingThread;
    private final PersonDetection personDetectionThread;
    private final FacialScanner facialScannerThread;

    // Passenger Queue
    private final BlockingQueue<Passenger> passengerQueue;

    private final HashMap<String, List<Observer>> subscribers;
    private final HashMap<String, Callable<Consumer<Response>>> eventsResponses;
    private final TransferQueue<Response> eventsReceived;

    // Events Emitted
    public static final String addedNewPassenger = "NewPassenger";

    public AutomatedPassportControlSystem() {
        isRunning = new AtomicBoolean(true);
        passengerQueue = new LinkedBlockingQueue<>();

        subscribers = new HashMap<>();
        subscribers.put(AutomatedPassportControlSystem.addedNewPassenger, new ArrayList<>());
        eventsReceived = new LinkedTransferQueue<>();
        eventsResponses = new HashMap<>();
        threadPool = Executors.newScheduledThreadPool(8);

        // Responses to events
        // Event 1 : Closed exit -> Append new passenger
        eventsResponses.put(GateControl.closedExit, () -> {
            return (res) -> {
                if (isRunning.get() || passengerQueue.size() > 0) {
                    Response result = Utility.createOkResponse(AutomatedPassportControlSystem.addedNewPassenger);
                    try {
                        result.setCurrentPassenger(passengerQueue.poll(1, TimeUnit.MINUTES));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (Observer subscriber : subscribers.get(AutomatedPassportControlSystem.addedNewPassenger)) {
                        subscriber.sendEvent(result);
                    }
                } else {
                    // shutdown all threads, the system simulated is closed.
                    threadPool.shutdownNow();
                }
            };
        });

        gateControlThread = new GateControl();
        dataProcessingThread = new DataProcessing();
        passportScannerThread = new PassportScanner();
        thumbprintScannerThread = new ThumbprintScanner();
        personDetectionThread = new PersonDetection();
        facialScannerThread = new FacialScanner();

        // Registering all the observers for each thread for the simulation
        this.addObserver(AutomatedPassportControlSystem.addedNewPassenger, passportScannerThread);
        passportScannerThread.addObserver(PassportScanner.getIssuer, dataProcessingThread);
        dataProcessingThread.addObserver(DataProcessing.validatedPassport, gateControlThread);
        gateControlThread.addObserver(GateControl.openedEntry, personDetectionThread);
        personDetectionThread.addObserver(PersonDetection.onePersonDetected, gateControlThread);
        gateControlThread.addObserver(GateControl.closedEntry, passportScannerThread);
        gateControlThread.addObserver(GateControl.closedEntry, thumbprintScannerThread);
        gateControlThread.addObserver(GateControl.closedEntry, facialScannerThread);
        facialScannerThread.addObserver(FacialScanner.getFacial, dataProcessingThread);
        thumbprintScannerThread.addObserver(ThumbprintScanner.getThumbprint, dataProcessingThread);
        passportScannerThread.addObserver(PassportScanner.getData, dataProcessingThread);
        dataProcessingThread.addObserver(DataProcessing.verifiedPassenger, gateControlThread);
        gateControlThread.addObserver(GateControl.openedExit, personDetectionThread);
        personDetectionThread.addObserver(PersonDetection.nonePersonDetected, gateControlThread);
        gateControlThread.addObserver(GateControl.closedExit, this);

    }

    /* Description : This run method will retrieve the responses received and fetch and execute the correct event
     *  response to the event. Event responses are stored in a consumer function wrapped in a callable. */
    @Override
    public void run() {
        try {
            Response receivedEvent = eventsReceived.take();
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

    /* Description : This function is called to officially start the system and simulation */
    public void start() throws InterruptedException {
        // Schedule all relevant threads to run
        threadPool.scheduleWithFixedDelay(this, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(passportScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(dataProcessingThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(personDetectionThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(gateControlThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(facialScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(thumbprintScannerThread, 0, 10, TimeUnit.MILLISECONDS);

        // bootstrap for first passenger
        Response res = Utility.createOkResponse(AutomatedPassportControlSystem.addedNewPassenger);
        res.setCurrentPassenger(passengerQueue.poll(1, TimeUnit.MINUTES));

        for (Observer subscriber : subscribers.get(AutomatedPassportControlSystem.addedNewPassenger)) {
            subscriber.sendEvent(res);
        }
    }

    public void addPassenger(Passenger passenger) {
        passengerQueue.add(passenger);
    }

    public void closeTerminal() {
        isRunning.set(false);
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
        return "AutomatedPassportControlSystem";
    }
}








