import java.util.NoSuchElementException;


/*
    Class Name  : DepartureCoordinator
    Description : A thread that will alternate between LandingCoordinator to make sure only one airplane is given permission to land/depart.
*/


public class DepartureCoordinator implements Runnable {
    private AirportTrafficController airportTrafficController;

    public DepartureCoordinator(AirportTrafficController airportTrafficController) {
        this.airportTrafficController = airportTrafficController;
    }

    /*
    Method name : run (Method to be called when thread is started)
    Parameter   : Null
    Description : Direct the airplane to depart from runway and record the time taken for the airplane to depart
    Return      : Null
    */
    @Override
    public void run() {
        synchronized (airportTrafficController.runway) {
            while (true) {
                try {
                    // start off by waiting (will be notified by Airport Traffic Controller shortly)
                    airportTrafficController.runway.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Throws exception when the queue is empty
                    Airplane airplaneToDepart = airportTrafficController.getDepartureQueue().remove();
                    System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToDepart.getName() + " has the permission to take off now.");
                    // Let airplane to depart and wait for its operation to finish
                    airplaneToDepart.performTakeOff.start();
                    try {
                        airplaneToDepart.performTakeOff.join();
                    } catch (InterruptedException e) {
                        System.out.println("Takeoff operation interrupted unexpectedly. Check simulation program!!");
                        e.printStackTrace();
                        continue;
                    }
                    airplaneToDepart.endTimer();
                    airportTrafficController.addTakeoffTime(airplaneToDepart.getElapsedTime());
                    airplaneToDepart.startTimer();
                    System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToDepart.getName() + " have a safe trip.");
                    airportTrafficController.incrementDepartedAirplane();
                } catch (NoSuchElementException ignored) {
                    // No airplane waiting to depart, thread can go back to sleep.
                }
                // Wake landing coordinator since runway is free
                airportTrafficController.runway.notify();
                // If below two conditions are satisfied, then this thread will terminate 
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0) {
                    return;
                }
            }
        }
    }
}
