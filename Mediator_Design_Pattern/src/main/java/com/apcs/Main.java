package com.apcs;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    @Benchmark
    @Fork(1)
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    public void startAPCS() {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread automatedPassportControlSystem = new Thread(apcs);
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 5));
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

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(Main.class.getSimpleName()).build();
        new Runner(opt).run();
//        startAPCS();
    }
}
