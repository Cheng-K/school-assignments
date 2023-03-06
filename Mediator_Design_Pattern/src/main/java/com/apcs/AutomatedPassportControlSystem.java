package com.apcs;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/* Description : This class is the entry point of the simulated automated passport control system and should be
 *  instantiated to run this simulation. In the simulation, this class is a mediator thread that manage all the
 *  communications between threads and send instructions to the threads for them to execute. Instructions are sent as
 *  callable. This thread is also the entry point for a passenger first use the simulated system. */

public class AutomatedPassportControlSystem implements Runnable {
    public final AtomicBoolean isRunning = new AtomicBoolean(true);
    // -------- Communication Channels to subcomponents -------------------------
    private final TransferQueue<Callable<Response>> instructionsToGateControl;
    private final TransferQueue<Response> outputFromGateControl;
    private final TransferQueue<Callable<Response>> instructionsToPassportScanner;
    private final TransferQueue<Response> outputFromPassportScanner;
    private final TransferQueue<Callable<Response>> instructionsToThumbprintScanner;
    private final TransferQueue<Response> outputFromThumbprintScanner;
    private final TransferQueue<Callable<Response>> instructionsToFacialScanner;
    private final TransferQueue<Response> outputFromFacialScanner;
    private final TransferQueue<Callable<Response>> instructionsToDataProcessing;
    private final TransferQueue<Response> outputFromDataProcessing;
    private final TransferQueue<Callable<Response>> instructionsToPersonDetection;
    private final TransferQueue<Response> outputFromPersonDetection;

    private final ScheduledExecutorService threadPool;
    // Sub components
    private final GateControl gateControlThread;
    private final PassportScanner passportScannerThread;
    private final ThumbprintScanner thumbprintScannerThread;
    private final DataProcessing dataProcessingThread;
    private final PersonDetection personDetectionThread;
    private final FacialScanner facialScannerThread;

    // Passenger Queue
    private final BlockingQueue<Passenger> passengerQueue = new LinkedBlockingQueue<>();


    public AutomatedPassportControlSystem() {
        instructionsToGateControl = new LinkedTransferQueue<>();
        outputFromGateControl = new LinkedTransferQueue<>();
        instructionsToPassportScanner = new LinkedTransferQueue<>();
        outputFromPassportScanner = new LinkedTransferQueue<>();
        instructionsToThumbprintScanner = new LinkedTransferQueue<>();
        outputFromThumbprintScanner = new LinkedTransferQueue<>();
        instructionsToDataProcessing = new LinkedTransferQueue<>();
        outputFromDataProcessing = new LinkedTransferQueue<>();
        instructionsToPersonDetection = new LinkedTransferQueue<>();
        outputFromPersonDetection = new LinkedTransferQueue<>();
        instructionsToFacialScanner = new LinkedTransferQueue<>();
        outputFromFacialScanner = new LinkedTransferQueue<>();

        threadPool = Executors.newScheduledThreadPool(15);
        gateControlThread = new GateControl(instructionsToGateControl, outputFromGateControl);
        passportScannerThread = new PassportScanner(instructionsToPassportScanner, outputFromPassportScanner);
        thumbprintScannerThread = new ThumbprintScanner(instructionsToThumbprintScanner, outputFromThumbprintScanner);
        dataProcessingThread = new DataProcessing(instructionsToDataProcessing, outputFromDataProcessing);
        personDetectionThread = new PersonDetection(instructionsToPersonDetection, outputFromPersonDetection);
        facialScannerThread = new FacialScanner(instructionsToFacialScanner, outputFromFacialScanner);

    }

    /* Description : This run method will first schedule all the sub threads to run at a fixed delay so that all the
     *  sub threads are ready for next operation after completing an operation. Since it is a mediator thread, it will
     *  manage the overall process of the handling a passenger by sending instructions to all the sub threads and
     *  process their responses. The thread dies when the simulated system is not running
     *  (no more new passengers generated) and the passenger queue is empty. */
    @Override
    public void run() {
        // Schedule all relevant threads to run
        threadPool.scheduleWithFixedDelay(gateControlThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(personDetectionThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(facialScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(thumbprintScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(dataProcessingThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(passportScannerThread, 0, 10, TimeUnit.MILLISECONDS);

        // Keep running if the system is still running or the passenger queue size is not empty
        while (isRunning.get() || passengerQueue.size() > 0) {
            final Passenger currentPassenger;
            Response res;
            // Below is the entire flow of a passenger using the system.
            try {
                currentPassenger = passengerQueue.take();
                currentPassenger.scanPassport();
                // Validating the passport before letting passenger into the platform
                System.out.println("APCS : Scanning your passport... Please do not remove your passport from the scanner.");
                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportIssuer(currentPassenger.getPassport()));
                res = outputFromPassportScanner.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new RuntimeException("Unable to scan passenger passports");
                }
                System.out.println("APCS : Done. You can remove your passport from the scanner. Validating your passport...");
                byte[] passportIssuer = res.getData();
                res = null;
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.validatePassport(passportIssuer));
                if (outputFromDataProcessing.take().getStatus() != Response.STATUS.OK) {
                    throw new RuntimeException("Unable to validate passenger passport issuer");
                }
                System.out.println("APCS : Validated. Please step inside the platform after the gate opened");
                // Passenger walks into the platform
                instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
                if (outputFromGateControl.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to open entry gate");
                currentPassenger.enterPlatform();
                personDetectionThread.setTotalPerson(1);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyOnlyOnePerson());
                if (outputFromPersonDetection.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("More than one people detected");
                instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
                if (outputFromGateControl.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to close entry gate");
                System.out.println("APCS : Entry gate closed.");

                // Getting passenger and passport data
                System.out.println("APCS : Please place your passport onto the scanner while your thumb on thumbprint scanner and look into the camera in front of you.");
                currentPassenger.scanPassport();
                currentPassenger.scanThumb();
                System.out.println("APCS : Scanning your face... Please look into the camera in front of you.");
                System.out.println("APCS : Scanning your thumbprint... Please do not remove your thumb from the scanner.");
                System.out.println("APCS : Scanning your passport... Please do not remove your passport from the scanner.");

                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportData(currentPassenger.getPassport()));
                instructionsToFacialScanner.transfer(() -> facialScannerThread.getFacialData(currentPassenger));
                instructionsToThumbprintScanner.transfer(() -> thumbprintScannerThread.getThumbprintData(currentPassenger));
                Response fromPassportScanner = outputFromPassportScanner.take();
                Response fromFacialScanner = outputFromFacialScanner.take();
                Response fromThumbprintScanner = outputFromThumbprintScanner.take();

                if (fromPassportScanner.getStatus() != Response.STATUS.OK || fromFacialScanner.getStatus() != Response.STATUS.OK || fromThumbprintScanner.getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to get passport or passenger data");

                System.out.println("APCS : Done. You can remove your passport and thumb from the scanners.");
                byte[] passportData = fromPassportScanner.getData();
                byte[] facialData = fromFacialScanner.getData();
                byte[] thumbprintData = fromThumbprintScanner.getData();

                // Verification of passport, facial, and thumbprint data
                System.out.println("APCS : Verifying your credentials... ");
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.verifyPassportWithFacial(passportData, facialData));
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.verifyPassportWithThumbprint(passportData, thumbprintData));
                Response facialVerification = outputFromDataProcessing.take();
                Response thumbprintVerification = outputFromDataProcessing.take();

                if (facialVerification.getStatus() != Response.STATUS.OK && thumbprintVerification.getStatus() != Response.STATUS.OK) {
                    throw new RuntimeException("Passenger passport cannot be verified");
                }
                // Uploading and allow passenger to exit the platform
                System.out.println("APCS : Credentials successfully verified... Please wait for the system to upload your data.");
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.uploadData(currentPassenger.getPassport()));
                if (outputFromDataProcessing.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to upload data to cloud server");
                System.out.println("APCS : Done. Please exit the platform once the exit gate is opened.");
                instructionsToGateControl.transfer(() -> gateControlThread.openExit());
                if (outputFromGateControl.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to open exit gate");
                System.out.println("APCS : Exit gate opened");
                currentPassenger.exitPlatform();
                personDetectionThread.setTotalPerson(0);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyNoPerson());
                if (outputFromPersonDetection.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to verify no person left in the platform");
                instructionsToGateControl.transfer(() -> gateControlThread.closeExit());
                if (outputFromGateControl.take().getStatus() != Response.STATUS.OK)
                    throw new RuntimeException("Unable to close exit gate");
                System.out.println("APCS : Exit gate closed.");
            } catch (InterruptedException e) {
                System.err.println("WARNING : " + this + " has been interrupted and terminated.");
            } catch (Throwable e) {
                System.err.println("ERROR : ");
                e.printStackTrace();
            }
        }
        // shutdown all threads, the system simulated is closed.
        threadPool.shutdownNow();

    }

    public void addPassenger(Passenger passenger) {
        passengerQueue.add(passenger);
    }

    public void closeTerminal() {
        isRunning.set(false);
    }
}






