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
    /*
    Method name : run (Method to be called when thread is started)
    Parameter   : Null
    Description : Direct the airplane to land at runway and record the time taken for the airplane to land
    Return      : Null
    */
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
                        airplaneToLand.endTimer();
                        airportTrafficController.addLandingTime(airplaneToLand.getElapsedTime());
                        airplaneToLand.startTimer();
                        if (airplaneToLand.requireEmergencyAttention())
                            airportTrafficController.getDockingQueue().addFirst(airplaneToLand);
                        else
                            airportTrafficController.getDockingQueue().addLast(airplaneToLand);

                    }
                    catch (NoSuchElementException ignored){
                        // No airplanes are waiting for landing, thread can go back to sleep after this.
                    }
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " : Unable to land any airplanes due to docking queue is at full capacity.");
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
