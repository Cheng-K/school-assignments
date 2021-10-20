import java.util.NoSuchElementException;

/*
    Class Name  : LandingCoordinator
    Description : A thread that will alternate between DepartureCoordinator to make sure only one airplane is given permission to land/depart.
*/


public class LandingCoordinator implements Runnable{
    private final AirportTrafficController airportTrafficController;

    public LandingCoordinator (AirportTrafficController airportTrafficController){
        this.airportTrafficController = airportTrafficController;
    }
    @Override
    public void run() {
        synchronized (airportTrafficController.runway){
            while (true){
                try {
                    // start off by waiting (will be notified by Airport Traffic Controller shortly)
                    airportTrafficController.runway.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (airportTrafficController.getDockingQueue().size() < 2){
                    try {
                        // Throws an exception if the queue is empty
                        Airplane airplaneToLand = airportTrafficController.getLandingQueue().removeFirst();
                        System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToLand.getName() + " has the permission to land now.");
                        // Let airplane perform landing and wait
                        airplaneToLand.performLanding.start();
                        try {
                            airplaneToLand.performLanding.join();
                        } catch (InterruptedException error){
                            System.out.println("Landing operation interrupted unexpectedly. Check simulation program !!");
                            error.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToLand.getName() + " please exit the runway and join the docking queue to wait for instructions to dock at specific gateway.");
                        airplaneToLand.postLandingReply();
                        airportTrafficController.getDockingQueue().addLast(airplaneToLand);
                    }
                    catch (NoSuchElementException exception){
                        // Catches exception where the landing queue is empty 
                    }
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " : Unable to land an airplane due to docking queue is at full capacity.");
                }
                // Wake DepartureCoordinator since runway is free
                airportTrafficController.runway.notify();
                // This thread will terminate its operation if below two conditions are satisfied
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0)
                    return;
            }

        }
    }
}
