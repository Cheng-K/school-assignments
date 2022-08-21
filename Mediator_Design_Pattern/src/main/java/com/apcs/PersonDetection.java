package com.apcs;

import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

public class PersonDetection implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private int totalPerson = 0;

    public PersonDetection(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
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
