/*
    Author : Ong Cheng Kei TP055620
    Description : This file contains the main class which starts the program.
 */


/* 
    Next Task : Test & Optimize the program
               : Implement specific gateway
*/

public class Main {
    public static void main(String[] args) {
        AirportTrafficController airportTrafficController = new AirportTrafficController();
        airportTrafficController.start();
        AirplaneGenerator generator = new AirplaneGenerator(10,airportTrafficController);
        generator.start();
        try {
            airportTrafficController.join();
        } catch (InterruptedException e){
            System.out.println("Program crashed at main class. Check simulation program!!");
        }
    }
}
