import java.util.NoSuchElementException;

public class UndockingCoordinator implements Runnable{
    private final AirportTrafficController airportTrafficController;

    public UndockingCoordinator(AirportTrafficController airportTrafficController) {
        this.airportTrafficController = airportTrafficController;
    }

    @Override
    public void run() {
        synchronized (airportTrafficController.landTrafficController){
            while (true){
                try {
                    airportTrafficController.landTrafficController.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (airportTrafficController.getDepartureQueue().size() < 2){
                    try {
                        Airplane airplaneToUndock = airportTrafficController.getUndockingQueue().remove();
                        System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToUndock.getName() + " has the permission to undock now.");
                        airplaneToUndock.performUndocking.start();
                        try {
                            airplaneToUndock.performUndocking.join();
                        } catch (InterruptedException e){
                            System.out.println("Undocking operation interrupted unexpectedly.");
                            e.printStackTrace();
                            continue;
                        }
                        airportTrafficController.availableGateways.release();
                        System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToUndock.getName() + " please proceed to join the departure queue beside the runway.");
                        airplaneToUndock.postUndockReply();
                        airportTrafficController.getDepartureQueue().add(airplaneToUndock);
                    }
                    catch (NoSuchElementException ex){
//                        System.out.println(Thread.currentThread().getName() + " : There are currently no airplane waiting to be undock.");
                    }
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " : Unable to undock any airplane due to full capacity in departure queue.");
                }
                airportTrafficController.landTrafficController.notify();
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0)
                    return;
            }
        }
    }
}
