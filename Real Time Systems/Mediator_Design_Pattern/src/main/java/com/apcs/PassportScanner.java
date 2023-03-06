package com.apcs;

import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

/*Description : PassportScanner is one of the subcomponents of the simulated system. It is a thread that performs
 * passport scanning operations. All operations are represented as a method in this class.
 * */
public class PassportScanner implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;

    public PassportScanner(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
        this.instructionsQueue = instructionsQueue;
        this.resultOutput = resultOutput;
    }

    /* Description : This run method will retrieve and execute the instructions received .
     *  Instructions are in the form of callable and the output from the instructions will be inserted into the
     *  resultOutput queue which will be acknowledged by the mediator thread */
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
            System.err.print("ERROR : ");
            e.printStackTrace();
        }
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response getPassportData(Passport passport) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // processing to get passport data
        return Utility.createOkResponse(passport.getData());
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response getPassportIssuer(Passport passport) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Utility.createOkResponse(passport.getIssuer());
    }
}
