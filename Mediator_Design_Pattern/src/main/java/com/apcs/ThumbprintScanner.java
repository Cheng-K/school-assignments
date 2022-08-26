package com.apcs;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

public class ThumbprintScanner implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    ThumbprintScanner(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    @Override
    public void run() {
        try {
            Callable<Response> instructions;
            Response response;
            instructions = instructionsQueue.take();
            response = instructions.call();
            resultOutput.put(response);
        } catch (InterruptedException e) {
            System.err.println("WARNING : " + this + " has been interrupted and terminated.");
        } catch (Throwable e) {
            System.err.println("ERROR : ");
            e.printStackTrace();
        }
    }

    public Response getThumbprintData(Passenger passenger) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(passenger.getThumbprintData().getBytes(StandardCharsets.UTF_8));
    }
}
