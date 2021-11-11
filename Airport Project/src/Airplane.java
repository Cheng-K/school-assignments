
/*
    Class name  : Airplane
    Description : This class contains essential methods for airplane to report themselves to Airport Traffic Controller, and 
                  also four threads that perform necessary airplane operations (dock/undock/takeoff/landing)
*/

import java.util.concurrent.ThreadLocalRandom;

public class Airplane {

    private final String name;
    private final AirportTrafficController controller;
    private final boolean hasEmergencies;
    private String assignedGateway;   // name of gateway to dock (assigned by airport traffic controller)
    public final Thread performLanding;
    public final Thread performTakeOff;
    public final Thread performDocking;
    public final Thread performUndocking;
    public final Thread performUnloadAndLoad;
    private final int numberOfPassengersDisembarked;
    private final int numberOfPassengersBoarded;
    private long startTime;
    private long endTime;


    /* Private classes encapsulated in the airplane class represent individual tasks that an airplane will perform throughout the program.*/

    private class Landing implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " : Copy that. " + Thread.currentThread().getName() + " is preparing for landing.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " crashed unexpectedly.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " landed successfully.");
        }
    }

    private class Departure implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " : Airplane " + Thread.currentThread().getName() +
                    " is preparing for takeoff.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " departure process interrupted.");
            }
            System.out.println(Thread.currentThread().getName() + " : Airplane " + Thread.currentThread().getName() +
                    " departed successfully. Thanks.");
        }
    }

    private class Dock implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is preparing to dock at Gateway : " + assignedGateway);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " docking process interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " docked successfully at Gateway : " + assignedGateway);
        }
    }

    private class UnloadAndLoad implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is letting passengers to disembark and unloading cargo.");
            try {
                Thread.sleep((long) (numberOfPassengersDisembarked * 0.1));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " unloading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : A total of " + numberOfPassengersDisembarked + " passengers disembark from " +
                    "the airplane, all cargo supplies successfully unloaded.");
            controller.addPassengersArrived(numberOfPassengersDisembarked);
            System.out.println(Thread.currentThread().getName() + " refilling supplies and letting passengers to board the airplane now.");
            try {
                Thread.sleep((long) (numberOfPassengersBoarded * 0.1));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " loading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : A total of " + numberOfPassengersBoarded + " passengers boarded the airplane, " +
                    "all cargo supplies successfully loaded.");
            controller.addPassengersDeparted(numberOfPassengersBoarded);
            requestUndockAndTakeOff();
        }
    }

    private class Undock implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is preparing to undock from Gateway : " + assignedGateway);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " undocking process interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " undocked successfully from Gateway : " + assignedGateway);
        }
    }


    public Airplane(String name, AirportTrafficController controller, boolean hasEmergencies) {
        this.name = name;
        this.controller = controller;
        this.hasEmergencies = hasEmergencies;
        performLanding = new Thread(new Landing(), name);
        performTakeOff = new Thread(new Departure(), name);
        performDocking = new Thread(new Dock(), name);
        performUndocking = new Thread(new Undock(), name);
        performUnloadAndLoad = new Thread(new UnloadAndLoad(), name);
        numberOfPassengersDisembarked = ThreadLocalRandom.current().nextInt(25, 51);
        numberOfPassengersBoarded = ThreadLocalRandom.current().nextInt(25, 51);
        requestLanding();
    }

    /*
        Method name : requestLanding
        Parameter   : -
        Description : Notify the airport traffic controller to add this airplane to landing queue.
    */
    public void requestLanding() {
        String requestString;
        if (hasEmergencies)
            requestString = " : 🔴 Mayday! Mayday! Mayday! Requesting for emergency landing at the airport if possible.";
        else
            requestString = " : Approaching airport in 20 minutes. Requesting permission to land on runway.";
        Thread initialConnection = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + requestString);
            controller.addAirplaneToLandingQueue(this);
            System.out.println(Thread.currentThread().getName() + " : Copy that. Joining the landing queue now.");
        }, name);
        initialConnection.start();
        startTimer();
    }

    /*
        Method name : postLandingReply
        Parameter   : -
        Description : Notify the airport traffic controller that this airplane successfully landed.
    */

    public void postLandingReply() {
        Thread replyThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " : Copy that. Exiting the runway and joining the docking queue now.");
        }, name);
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unexpected Interruption occurred.");
            e.printStackTrace();
        }
    }

    /*
        Method name : requestUndockAndTakeOff
        Parameter   : -
        Description : Notify the airport traffic controller to add this airplane to undock queue. This is called after airplane finish loading supplies.
    */
    private void requestUndockAndTakeOff() {
        System.out.println(Thread.currentThread().getName() + " : Requesting permission to undock and take off from airport.");
        controller.addAirplaneToUndockQueue(this);
        System.out.println(Thread.currentThread().getName() + " : Copy that. Will wait for further instructions.");

    }
    /*
        Method name : postUndockReply
        Parameter   : -
        Description : Notify the airport traffic controller that this airplane successfully undock.
    */

    public void postUndockReply() {
        Thread replyThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " : Copy that. Joining the departure queue now.");
        }, name);
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unexpected Interruption occurred.");
            e.printStackTrace();
        }
    }

    /* Getters & Setters */

    public void setAssignedGateway(String assignedGateway) {
        this.assignedGateway = assignedGateway;
    }

    public String getAssignedGateway() {
        return assignedGateway;
    }

    public boolean requireEmergencyAttention() {
        return hasEmergencies;
    }

    public String getName() {
        return name;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void endTimer() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return (endTime - startTime) / 1000;
    }


}
