package com.apcs;

public class VisualVMProfiler {
    public static void startAPCSMediatorProfiling () {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread automatedPassportControlSystem = new Thread(apcs);
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 10));
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
        startAPCSMediatorProfiling();
    }

}
