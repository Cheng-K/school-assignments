package com.apcs;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class JMHBenchmark {
    @Benchmark
    @Fork(1)
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @Warmup(iterations = 5)
    @Measurement(iterations = 50)
    @OutputTimeUnit(TimeUnit.MINUTES)
    public void startAPCSMediator() {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread automatedPassportControlSystem = new Thread(apcs);
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs, 1));
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
        Options opt = new OptionsBuilder().include(JMHBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
