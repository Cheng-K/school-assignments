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
    private BlockingQueue<Passenger> passengerQueue = new LinkedBlockingQueue<>();


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
        personDetectionThread = new PersonDetection(instructionsToPersonDetection,outputFromPersonDetection);
        facialScannerThread = new FacialScanner(instructionsToFacialScanner,outputFromFacialScanner);

    }

    @Override
    public void run() {
        // Schedule all relevant threads to run
        threadPool.scheduleWithFixedDelay(gateControlThread, 0, 10, TimeUnit.MILLISECONDS);
        threadPool.scheduleWithFixedDelay(passportScannerThread, 0, 10, TimeUnit.MILLISECONDS);

        // Testing
        while (isRunning.get() || passengerQueue.size() > 0) {
            Passenger currentPassenger;
            Response res = null;

            try {
                currentPassenger = passengerQueue.take();
                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportIssuer(currentPassenger.getPassport()));
                res = outputFromPassportScanner.take();
                if (res.getStatus() != Response.STATUS.OK){
                    // reject the current passenger
                    System.out.println("Passport incompatible to be used on this control system... Please proceed to the correct channels for your passport");
                    continue;
                }
                instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
                res = outputFromGateControl.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new InterruptedException("Unable to open entry gate");
                }
                personDetectionThread.setTotalPerson(1);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyOnlyOnePerson());
                res = outputFromPersonDetection.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new InterruptedException("More than one people detected");
                }
                instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new InterruptedException("Unable to close entry gate");
                }
                instructionsToPassportScanner.transfer(() -> passportScannerThread.getPassportData(currentPassenger.getPassport()));
                instructionsToFacialScanner.transfer(() -> facialScannerThread.getFacialData(currentPassenger));
                instructionsToThumbprintScanner.transfer(() -> thumbprintScannerThread.getThumbprintData(currentPassenger));
                byte[] passportData = outputFromPassportScanner.take().getData();
                byte[] facialData = outputFromFacialScanner.take().getData();
                byte[] thumbprintData = outputFromThumbprintScanner.take().getData();
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.verifyPassportWithFacial(passportData,facialData));
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.verifyPassportWithThumbprint(passportData,thumbprintData));
                res = outputFromDataProcessing.take();

                if (res.getStatus() != Response.STATUS.OK && outputFromDataProcessing.take().getStatus() != Response.STATUS.OK) {
                    // reject passenger
                    System.out.println("Passport cannot be verified... Please find help from official personnel nearby");
                    instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
                    if (res.getStatus() != Response.STATUS.OK) {
                        throw new InterruptedException("Unable to open entry gate");
                    }
                    personDetectionThread.setTotalPerson(0);
                    instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyNoPerson());
                    res = outputFromPersonDetection.take();
                    if (res.getStatus() != Response.STATUS.OK) {
                        throw new InterruptedException("Person refused to leave");
                    }
                    instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
                    if (res.getStatus() != Response.STATUS.OK) {
                        throw new InterruptedException("Unable to close entry gate");
                    }
                    continue;
                }
                instructionsToDataProcessing.transfer(() -> dataProcessingThread.uploadData(currentPassenger.getPassport()));
                instructionsToGateControl.transfer(() -> gateControlThread.openExit());
                personDetectionThread.setTotalPerson(0);
                instructionsToPersonDetection.transfer(() -> personDetectionThread.verifyNoPerson());
                res = outputFromPersonDetection.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new InterruptedException("Person refused to leave");
                }
                instructionsToGateControl.transfer(() -> gateControlThread.closeExit());
                res = outputFromDataProcessing.take();
                if (res.getStatus() != Response.STATUS.OK) {
                    throw new InterruptedException("Data processing unable to upload data");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        threadPool.shutdown();
    }

    public void addPassenger (Passenger passenger) {
        passengerQueue.add(passenger);
    }
    public void closeTerminal () {
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
        System.out.println("Entry gate opened.");
        return Utility.createOkResponse();
    }

    public Response closeEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Entry gate closed.");
        return Utility.createOkResponse();
    }

    public Response openExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exit gate opened.");
        return Utility.createOkResponse();
    }

    public Response closeExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exit gate closed.");
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
        System.out.println("Scanning your passport... Please do not remove your passport from the scanner.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // processing to get passport data
        memoryBuffer = passport.getData();
        System.out.println("Done. You can remove your passport from the scanner");
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
        System.out.println("Scanning your thumbprint... Please do not remove your thumb from the scanner.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memoryBuffer = passenger.getThumbprintData().getBytes(StandardCharsets.UTF_8);
        System.out.println("Done. You can remove your thumb from the scanner.");
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
        System.out.println("Scanning your face... Please look into the camera in front of you.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memoryBuffer = passenger.getFacialData().getBytes(StandardCharsets.UTF_8);
        System.out.println("Done.");
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
