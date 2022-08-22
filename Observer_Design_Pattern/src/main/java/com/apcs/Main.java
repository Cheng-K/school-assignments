package com.apcs;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class Main {
    @Benchmark
    @Fork(1)
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void startAPCS() {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 3));
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

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(Main.class.getSimpleName()).build();
        new Runner(opt).run();
//        startAPCS();
    }
}
