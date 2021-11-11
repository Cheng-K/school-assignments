/*
    Author : Ong Cheng Kei TP055620
    Intake : UC2102CS(DA)
    Module : Concurrent Programming CT074-3-2
    Description : This file contains the main method which starts the program.
 */


public class Main {
    public static void main(String[] args) {
        AirportTrafficController airportTrafficController = new AirportTrafficController();
        airportTrafficController.start();
        AirplaneGenerator generator = new AirplaneGenerator(10, airportTrafficController);
        generator.start();
        try {
            airportTrafficController.join();
        } catch (InterruptedException e) {
            System.out.println("Program crashed at main class. Check simulation program!!");
        }
        System.out.println("\n ------------ REPORT ------------ ");
        System.out.println("\nHere are all the estimated time taken (seconds) for each event during this simulation program...");
        airportTrafficController.generateReport();
    }
}
