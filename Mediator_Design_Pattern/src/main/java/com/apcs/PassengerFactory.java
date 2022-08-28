package com.apcs;

// This library is used for generating realistic fake names for simulated passengers

import com.github.javafaker.Faker;

/*Description: PassengerFactory is a factory class with a run method that instantiates x number of passengers
 * specified and insert them to the simulated system passenger's queue.
 * */
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
