
/*
    Class name  : Airplane
    Description : This class contains essential methods for airplane to report themselves to Airport Traffic Controller, and 
                  also four threads that perform necessary airplane operations (dock/undock/takeoff/landing)
*/

public class Airplane {

    private final String name;
    private final AirportTrafficController controller;
    private String assignedGateway;   // name of gateway to dock (assigned by airport traffic controller)
    public final Thread performLanding;
    public final Thread performTakeOff;
    public final Thread performDocking;
    public final Thread performUndocking;
    public final Thread performUnloadAndLoad;


    /* Private classes encapsulated in the airplane class represent individual tasks that an airplane will perform throughout the program.*/

    private class Landing implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " : Copy that. " +  Thread.currentThread().getName()+ " is preparing for landing.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " crashed unexpectedly.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " landed successfully");
        }
    }

    private class Departure implements Runnable {
        @Override
        public void run () {
            System.out.println(Thread.currentThread().getName() + " is preparing for takeoff.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName() + " departure process interrupted.");
            }
            System.out.println(Thread.currentThread().getName() + " departed successfully. Thanks.");
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
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " unloading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " passengers and cargo supplies successfully unloaded.");
            System.out.println(Thread.currentThread().getName() + " refilling supplies and letting passengers to board the airplane now.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " loading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished loading all necessary supplies.");
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


    public Airplane(String name,AirportTrafficController controller){
        this.name = name;
        this.controller = controller;
        performLanding = new Thread(new Landing(),name);
        performTakeOff = new Thread(new Departure(),name);
        performDocking = new Thread(new Dock(),name);
        performUndocking = new Thread(new Undock(),name);
        performUnloadAndLoad = new Thread(new UnloadAndLoad(),name);
        requestLanding();
    }

    /*
        Method name : requestLanding
        Parameter   : -
        Description : Notify the airport traffic controller to add this airplane to landing queue.
    */
    public void requestLanding () {
        Thread initialConnection = new Thread ( () -> {
            System.out.println(Thread.currentThread().getName() + " : Approaching airport in 20 minutes. Requesting permission to land on runway.");
            controller.addAirplaneToLandingQueue(this);
            System.out.println(Thread.currentThread().getName() + " : Copy that. Joining the landing queue now.");
        },name);
        initialConnection.start();
        // Maybe joining is necessary to really wait for airplane to get noticed by the airport. (Niche case : when the airport is closing the airplane might not get noticed)
    }

    /*
        Method name : postLandingReply
        Parameter   : -
        Description : Notify the airport traffic controller that this airplane successfully landed.
    */

    public void postLandingReply () {
        Thread replyThread = new Thread (() -> {
            System.out.println(Thread.currentThread().getName() + " : Copy that. Exiting the runway and joining the docking queue now.");
        },name);
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e){
            System.out.println("Unexpected Interruption occurred.");
            e.printStackTrace();
        }
    }

    /*
        Method name : requestUndockAndTakeOff
        Parameter   : -
        Description : Notify the airport traffic controller to add this airplane to undock queue. This is called after airplane finish loading supplies.
    */
    private void requestUndockAndTakeOff () {
        System.out.println(Thread.currentThread().getName() + " : Requesting permission to undock and take off from airport.");
        // Plug airport api here
        controller.addAirplaneToUndockQueue(this);
        System.out.println(Thread.currentThread().getName() + " : Copy that. Will wait for further instructions.");

    }
    /*
        Method name : postUndockReply
        Parameter   : -
        Description : Notify the airport traffic controller that this airplane successfully undock.
    */

    public void postUndockReply(){
         Thread replyThread = new Thread (() -> {
            System.out.println(Thread.currentThread().getName() + " : Copy that. Joining the departure queue now.");
        },name);
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e){
            System.out.println("Unexpected Interruption occurred.");
            e.printStackTrace();
        }       
    }

    /* Getters & Setters */
    
    public void setAssignedGateway (String assignedGateway){
        this.assignedGateway = assignedGateway;
    }

    public String getAssignedGateway (){
        return assignedGateway;
    }

    public String getName() {
        return name;
    }


}
