import java.util.NoSuchElementException;

/*
    Class Name  : DockingCoordinator
    Description : A thread that will coordinate between UndockingCoordinator to make sure only one airplane is given permission to dock/undock.
*/


public class DockingCoordinator implements Runnable{
    private final AirportTrafficController airportTrafficController;

    public DockingCoordinator(AirportTrafficController airportTrafficController){
        this.airportTrafficController = airportTrafficController;
    }
    @Override
    public void run() {
        synchronized (airportTrafficController.landTrafficController){
            while (true) {
                try {
                    airportTrafficController.landTrafficController.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (airportTrafficController.availableGateways.tryAcquire()){
                    try {
                        Airplane airplaneToDock = airportTrafficController.getDockingQueue().removeFirst();
                        String gatewayToDock = airportTrafficController.gateways.poll();
                        System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToDock.getName() + " has the permission to dock at gateway " + gatewayToDock);
                        airplaneToDock.setAssignedGateway(gatewayToDock);
                        airplaneToDock.performDocking.start();
                        try {
                            airplaneToDock.performDocking.join();
                        }catch (InterruptedException e){
                            System.out.println("Docking operation interrupted unexpectedly. Check simulation program !!");
                            e.printStackTrace();
                            continue;
                        }
                        airplaneToDock.endTimer();
                        airportTrafficController.setTotalDockingTime(airportTrafficController.getTotalDockingTime()+airplaneToDock.getElapsedTime());
                        airplaneToDock.startTimer();
                        System.out.println(Thread.currentThread().getName()+ " : Noted. Airplane " + airplaneToDock.getName() + " can start unload and refill supplies.");
                        airplaneToDock.performUnloadAndLoad.start();
                    }
                    catch (NoSuchElementException ex){
                        // release back the semaphore since it has already acquired a permit but does not have any airplane to dock at the moment
                        airportTrafficController.availableGateways.release();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " : Cannot dock any airplane due to no available gateways.");
                }

                airportTrafficController.landTrafficController.notify();
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0)
                    return;
            }
        }
    }
}
