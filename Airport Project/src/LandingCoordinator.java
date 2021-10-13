import java.util.NoSuchElementException;

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
                    airportTrafficController.runway.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (airportTrafficController.getDockingQueue().size() < 2){
                    try {
                        Airplane airplaneToLand = airportTrafficController.getLandingQueue().removeFirst();
                        System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToLand.getName() + " has the permission to land now.");
                        airplaneToLand.performLanding.start();
                        try {
                            airplaneToLand.performLanding.join();
                        } catch (InterruptedException error){
                            System.out.println("Landing operation interrupted unexpectedly. Check simulation program !!");
                            error.printStackTrace();
//                            continue;
                        }
                        System.out.println(Thread.currentThread().getName()+ " : Airplane " + airplaneToLand.getName() + " please exit the runway and join the docking queue to wait for instructions to dock at specific gateway.");
                        airplaneToLand.postLandingReply();
                        airportTrafficController.getDockingQueue().addLast(airplaneToLand);
                    }
                    catch (NoSuchElementException exception){
//                        System.out.println(Thread.currentThread().getName() + " : There are no incoming airplanes currently.");
                    }
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " : Unable to land an airplane due to docking queue is at full capacity.");
                }

                airportTrafficController.runway.notify();
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0)
                    return;
            }

        }
    }
}
