import java.util.NoSuchElementException;

public class DepartureCoordinator implements Runnable{
    private AirportTrafficController airportTrafficController;

    public DepartureCoordinator(AirportTrafficController airportTrafficController) {
        this.airportTrafficController = airportTrafficController;
    }

    @Override
    public void run(){
        synchronized (airportTrafficController.runway){
            while (true){
                try {
                    airportTrafficController.runway.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Throws exception when the queue is empty
                    Airplane airplaneToDepart = airportTrafficController.getDepartureQueue().remove();
                    System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToDepart.getName() + " has the permission to take off now.");
                    airplaneToDepart.performTakeOff.start();
                    try {
                        airplaneToDepart.performTakeOff.join();
                    } catch (InterruptedException e){
                        System.out.println("Takeoff operation interrupted unexpectedly. Check simulation program!!");
                        e.printStackTrace();
                        continue;
                    }
                    System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToDepart.getName() + " have a safe trip.");
                    airportTrafficController.incrementDepartedAirplane();
                }
                catch (NoSuchElementException e){
                    // Catch exception when queue is empty
                }
                airportTrafficController.runway.notify();
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0) {
                    return;
                }
            }
        }
    }
}
