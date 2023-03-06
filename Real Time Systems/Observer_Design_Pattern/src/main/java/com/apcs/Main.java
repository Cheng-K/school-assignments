package com.apcs;

import java.util.concurrent.TimeUnit;

/* Author : Ong Cheng Kei TP055620
 *  Intake : UC3F2205CS(DA)
 *  Module : CT087-3-3-RTS-Real Time Systems
 *  Description : This file contains the main method that starts the simulation. The awaitTermination is necessary
 *  to pause the main method while the underlying threads of APCS execute. Feel free to change the timeout according
 *  to the number of passengers simulated.
 * */

public class Main {
    public static void startAPCSProfiling() {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 50));
        passengerGenerator.start();
        try {
            apcs.start();
            passengerGenerator.join();
            apcs.threadPool.awaitTermination(20, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n ------------------- Simulation Program Terminated ---------------------------- ");
    }

    public static void main(String[] args) {
        startAPCSProfiling();
    }

}
