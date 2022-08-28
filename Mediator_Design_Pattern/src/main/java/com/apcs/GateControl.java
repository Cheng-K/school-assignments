package com.apcs;

import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

/* Description : This class is one of the subcomponents of the simulated system. It is a thread that performs controls
 * the entry and exit gate of the platform. All operations are represented as a method in this class. */

public class GateControl implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    public GateControl(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    /* Description : This run method will retrieve and execute the instructions received .
     *  Instructions are in the form of callable and the output from the instructions will be inserted into the
     *  resultOutput queue which will be acknowledged by the mediator thread. */
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

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response openEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response closeEntry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response openExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response closeExit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse();
    }
}

