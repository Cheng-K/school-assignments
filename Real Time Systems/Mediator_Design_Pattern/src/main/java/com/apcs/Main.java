package com.apcs;

/* Author : Ong Cheng Kei TP055620
 *  Intake : UC3F2205CS(DA)
 *  Module : CT087-3-3-RTS-Real Time Systems
 *  Description : This file contains the main method that starts the simulation. The join method will wait for the
 *  simulated scenario to end before the main method.
 * */
public class Main {
    public static void startAPCSProfiling() {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread automatedPassportControlSystem = new Thread(apcs);
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 50));
        automatedPassportControlSystem.start();
        passengerGenerator.start();
        try {
            passengerGenerator.join();
            automatedPassportControlSystem.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n ------------------- Simulation Program Terminated ---------------------------- ");

    }

    public static void main(String[] args) {
        startAPCSProfiling();
    }

}
