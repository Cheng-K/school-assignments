package com.apcs;

import com.github.javafaker.Faker;

public class PassengerFactory implements Runnable {
    private final AutomatedPassportControlSystem automatedPassportControlSystem;
    private final Faker nameGenerator;
    private final int numberOfPassenger;

    public PassengerFactory(AutomatedPassportControlSystem apcs, int numberOfPassenger) {
        automatedPassportControlSystem = apcs;
        this.numberOfPassenger = numberOfPassenger;
        nameGenerator = new Faker();
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfPassenger; i++) {
            automatedPassportControlSystem.addPassenger(new NormalPassenger(nameGenerator.name().fullName()));
        }
        automatedPassportControlSystem.closeTerminal();
    }
}
