package com.apcs;

import java.util.concurrent.TimeUnit;

public class VisualVMProfiler {
    public static void startAPCSObserverProfiling () {
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
        startAPCSObserverProfiling();
    }

}
