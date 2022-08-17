import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

//TODO : Implement all the tasks for all components and scanners
//TODO : Coordinate all tasks with scheduleWithFixedDelay (so that there will be no abundant of threads if one operation delayed)

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
    GateControl gateControlThread;
    PassportScanner passportScannerThread;
    ThumbprintScanner thumbprintScannerThread;
    DataProcessing dataProcessingThread;
    PersonDetection personDetectionThread;
    FacialScanner facialScannerThread;

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

    @Override
    public void run() {
        // Schedule all relevant threads to run
        threadPool.scheduleWithFixedDelay(gateControlThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(personDetectionThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(facialScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(thumbprintScannerThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(dataProcessingThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(passportScannerThread, 0, 10, TimeUnit.MILLISECONDS);


        while (isRunning.get() || passengerQueue.size() > 0) {
            final Passenger currentPassenger;
            Passenger copyPassenger = null;
            Response res;

            try {
                currentPassenger = passengerQueue.take();
                copyPassenger = currentPassenger;
                currentPassenger.scanPassport();
                // Validating the passport before letting passenger into the platform
                System.out.println("APCS : Scanning your passport... Please do not remove your passport from the scanner.");
                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportIssuer(currentPassenger.getPassport()));
                res = outputFromPassportScanner.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    System.out.println("APCS : Failed to scan passport data. Please find help with official personnel.");
                    continue;
                }
                System.out.println("APCS : Done. You can remove your passport from the scanner. Validating your passport...");

                byte[] passportIssuer = res.getData();
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.validatePassport(passportIssuer));
                res = outputFromDataProcessing.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    // reject the current passenger
                    System.out.println("APCS : Passport incompatible to be used on this control system... Please proceed to the correct channels for your passport");
                    continue;
                }
                System.out.println("APCS : Validated. Please step inside the platform after the gate opened");
                // Passenger walks into the platform
                instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
                res = outputFromGateControl.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to open entry gate");
                currentPassenger.enterPlatform();
                personDetectionThread.setTotalPerson(1);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyOnlyOnePerson());
                res = outputFromPersonDetection.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("More than one people detected");
                instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to close entry gate");
                System.out.println("APCS : Entry gate closed.");

                // Getting passenger and passport data
                System.out.println("APCS : Please place your passport onto the scanner while your thumb on thumbprint scanner and look into the camera in front of you.");
                currentPassenger.scanPassport();
                currentPassenger.scanThumb();
                System.out.println("APCS : Scanning your face... Please look into the camera in front of you.");
                System.out.println("APCS : Scanning your thumbprint... Please do not remove your thumb from the scanner.");

                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportData(currentPassenger.getPassport()));
                instructionsToFacialScanner.transfer(() -> facialScannerThread.getFacialData(currentPassenger));
                instructionsToThumbprintScanner.transfer(() -> thumbprintScannerThread.getThumbprintData(currentPassenger));
                Response fromPassportScanner = outputFromPassportScanner.take();
                Response fromFacialScanner = outputFromFacialScanner.take();
                Response fromThumbprintScanner = outputFromThumbprintScanner.take();

                if (fromPassportScanner.getStatus() != Response.STATUS.OK || fromFacialScanner.getStatus() != Response.STATUS.OK || fromThumbprintScanner.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to get passport or passenger data");

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
                    // reject passenger
                    System.out.println("APCS : Passport cannot be verified... Please find help from official personnel nearby");
                    instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
                    if (res.getStatus() != Response.STATUS.OK)
                        throw new InterruptedException("Unable to open entry gate");
                    currentPassenger.exitPlatform();
                    personDetectionThread.setTotalPerson(0);
                    instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyNoPerson());
                    res = outputFromPersonDetection.take();
                    if (res.getStatus() != Response.STATUS.OK)
                        throw new InterruptedException("Person refused to leave");
                    instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
                    if (res.getStatus() != Response.STATUS.OK)
                        throw new InterruptedException("Unable to close entry gate");
                    System.out.println("APCS : Entry gate closed.");
                    continue;
                }
                // Uploading and allow passenger to exit the platform
                System.out.println("APCS : Credentials successfully verified... Please exit once the exit gate opened.");
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.uploadData(currentPassenger.getPassport()));
                res = outputFromDataProcessing.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to upload data to cloud server");
                instructionsToGateControl.transfer(() -> gateControlThread.openExit());
                res = outputFromGateControl.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to open exit gate");
                currentPassenger.exitPlatform();
                personDetectionThread.setTotalPerson(0);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyNoPerson());
                res = outputFromPersonDetection.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Person refused to leave");
                instructionsToGateControl.transfer(() -> gateControlThread.closeExit());
                res = outputFromGateControl.take();
                if (res.getStatus() != Response.STATUS.OK)
                    throw new InterruptedException("Unable to close exit gate");
                System.out.println("APCS : Exit gate closed.");
            } catch (InterruptedException e) {
                // System will initiate a total reset
                System.out.println("APCS : Error encountered... Initiating a soft system reboot and reset... Passengers please exit from the entry gate..");
                System.out.println("APCS : Entry gate opened");
                if (copyPassenger != null) {
                    copyPassenger.exitPlatform();
                    this.addPassenger(copyPassenger);
                }
                System.out.println("APCS : Entry gate closed");
            }
        }
        threadPool.shutdownNow();
    }

    public void addPassenger(Passenger passenger) {
        passengerQueue.add(passenger);
    }

    public void closeTerminal() {
        isRunning.set(false);
    }
}

class GateControl implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    public GateControl(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        // wait for instructions
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }

        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Response openEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    public Response closeEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    public Response openExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    public Response closeExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }
}

class DataProcessing implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    public DataProcessing(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }
        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

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

    public Response uploadData(Passport passport) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }
}

class PassportScanner implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private byte[] memoryBuffer;

    public PassportScanner(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }
        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Response getPassportData(Passport passport) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // processing to get passport data
        memoryBuffer = passport.getData();
        return Utility.createOkResponse(memoryBuffer);
    }

    public Response getPassportIssuer(Passport passport) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memoryBuffer = passport.getIssuer();
        return Utility.createOkResponse(memoryBuffer);
    }
}

class ThumbprintScanner implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private byte[] memoryBuffer;

    ThumbprintScanner(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }
        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Response getThumbprintData(Passenger passenger) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memoryBuffer = passenger.getThumbprintData().getBytes(StandardCharsets.UTF_8);
        return Utility.createOkResponse(memoryBuffer);
    }
}

class FacialScanner implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private byte[] memoryBuffer;

    FacialScanner(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }
        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Response getFacialData(Passenger passenger) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memoryBuffer = passenger.getFacialData().getBytes(StandardCharsets.UTF_8);
        return Utility.createOkResponse(memoryBuffer);
    }
}

class PersonDetection implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private int totalPerson = 0;

    public PersonDetection(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        Callable<Response> instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = instructions.call();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }
        try {
            resultOutput.put(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Response verifyOnlyOnePerson() {
        return totalPerson == 1 ? Utility.createOkResponse() : Utility.createFailedResponse(new Exception("No person / Too many person detected."));
    }

    public Response verifyNoPerson() {
        return totalPerson == 0 ? Utility.createOkResponse() : Utility.createFailedResponse(new Exception("Person detected."));
    }

    public void setTotalPerson(int person) {
        totalPerson = person;
    }

}
