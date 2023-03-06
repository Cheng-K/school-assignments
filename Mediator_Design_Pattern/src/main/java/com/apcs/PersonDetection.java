package com.apcs;

import java.util.concurrent.Callable;
import java.util.concurrent.TransferQueue;

/*Description : PersonDetection is one of the subcomponents of the simulated system. It is a thread that performs
 * detection to ensure that no one is in the platform before closing exit gate and only one person is in the platform
 * before closing the entry gate. All operations are represented as a method in this class.
 * */
public class PersonDetection implements Runnable {
    private final TransferQueue<Callable<Response>> instructionsQueue;
    private final TransferQueue<Response> resultOutput;
    private int totalPerson = 0;

    public PersonDetection(TransferQueue<Callable<Response>> instructionsQueue, TransferQueue<Response> resultOutput) {
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
    public Response verifyOnlyOnePerson() {
        return totalPerson == 1 ? Utility.createOkResponse() : Utility.createFailedResponse(new Exception("No person / Too many person detected."));
    }

    /* Description : One of the simulated operation by this thread. All operations return a custom Response object */
    public Response verifyNoPerson() {
        return totalPerson == 0 ? Utility.createOkResponse() : Utility.createFailedResponse(new Exception("Person detected."));
    }

    public void setTotalPerson(int person) {
        totalPerson = person;
    }

}
