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
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void startAPCS() {
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

        // Testing encryption and decryption
//        String toEncrypt = "Halalled21rq";
//        byte[] encrypted = com.apcs.Utility.encryptData(toEncrypt.getBytes(StandardCharsets.UTF_8));
//        String decrypted = new String(com.apcs.Utility.decryptData(encrypted));
//        System.out.println("Original : " + toEncrypt);
//        System.out.println("Encrypted : " + new String(encrypted));
//        System.out.println("Decrypted : " + decrypted);

        // Testing signing and verifying
//        String toSign = "Lim";
//        byte[] signed;
//        try {
//            signed = com.apcs.Utility.signObject(toSign.getBytes(StandardCharsets.UTF_8));
//            System.out.println(Arrays.toString(signed));
//            System.out.println(com.apcs.Utility.verifyObject(signed));
//        } catch (SignatureException e) {
//            System.out.println("Unable to sign on the object");
//        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(Main.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
