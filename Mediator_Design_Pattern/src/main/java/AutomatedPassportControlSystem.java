import java.util.concurrent.*;

//TODO : Memory buffer across all scanners
//TODO : Figure out way to store passport data and facial data

public class AutomatedPassportControlSystem implements Runnable {
    private final TransferQueue<Runnable> instructionsToGateControl;
    private final TransferQueue<Response> incomingResponse;
    private ScheduledExecutorService threadPool;

    // Sub components
    GateControl gateControlThread;


    public AutomatedPassportControlSystem() {
        instructionsToGateControl = new LinkedTransferQueue<>();
        incomingResponse = new LinkedTransferQueue<>();
        threadPool = Executors.newScheduledThreadPool(15);
        gateControlThread = new GateControl(instructionsToGateControl,incomingResponse);

    }
    @Override
    public void run() {
        // Schedule all relevant threads to run
        threadPool.scheduleAtFixedRate(gateControlThread, 0, 10, TimeUnit.MILLISECONDS);

        // Testing
        Response r = null;
        try {
            instructionsToGateControl.transfer(() -> gateControlThread.openEntry());
            r = incomingResponse.take();
            if (r.getStatus() == Response.STATUS.OK) {
                instructionsToGateControl.transfer(() -> gateControlThread.closeEntry());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdown();
    }
}

class GateControl implements Runnable{
    private final TransferQueue<Runnable> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    public GateControl (TransferQueue<Runnable> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        // wait for instructions
        Runnable instructions;
        Response response;
        try {
            instructions = instructionsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            instructions.run();
            response = Utility.createOkResponse();
        } catch (Exception e) {
            response = Utility.createFailedResponse(e);
        }

        try {
            resultOutput.transfer(response);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void openEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Entry gate opened.");
    }
    public void closeEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Entry gate closed.");
    }
    public void openExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exit gate opened.");
    }
    public void closeExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exit gate closed.");
    }
}

class DataProcessing implements Runnable {
    private final TransferQueue<Runnable> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    public DataProcessing (TransferQueue<Runnable> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {}

    public void validatePassport (Passport passport) {}

    public void verifyPassportWithFacial (Passport passport, String facialData) {}

    public void verifyPassportWithThumbprint (Passport passport, String thumbprintData) {}

    public void uploadData (Passport passport, String data) {}
}

class PassportScanner implements Runnable {
    private final TransferQueue<Runnable> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private String memoryBuffer; // used to store passport data
    public PassportScanner (TransferQueue<Runnable> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }
    @Override
    public void run() {

    }

    public void getPassportData (Passport passport) {
        System.out.println("Scanning your passport... Please do not remove your passport from the scanner.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // processing to get passport data
        System.out.println("Done. You can remove your passport from the scanner");
    }
}

class ThumbprintScanner implements Runnable{
    @Override
    public void run() {

    }

    public void getThumbprintData () {
        System.out.println("Scanning your thumbprint... Please do not remove your thumb from the scanner.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done. You can remove your thumb from the scanner.");
    }
}

class FacialScanner implements Runnable {
    @Override
    public void run() {

    }

    public void getFacialData () {
        System.out.println("Scanning your face... Please look into the camera in front of you.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done.");
    }
}

class PersonDetection implements Runnable {
    @Override
    public void run() {

    }

    public void verifyOnlyOnePerson () {
        // Do some processing
    }
}
