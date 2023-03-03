import java.util.NoSuchElementException;

/*
    Class Name  : UndockingCoordinator
    Description : A thread that will coordinate between DockingCoordinator to make sure only one airplane is given permission to dock/undock.
*/

public class UndockingCoordinator implements Runnable {
    private final AirportTrafficController airportTrafficController;

    public UndockingCoordinator(AirportTrafficController airportTrafficController) {
        this.airportTrafficController = airportTrafficController;
    }

    /*
    Method name : run (Method to be called when thread is started)
    Parameter   : Null
    Description : Direct the airplane to undock from a specific gateway. Update the time taken, and the status of the gateway to available.
    Return      : Null
    */

    @Override
    public void run() {
        synchronized (airportTrafficController.landTrafficController) {
            while (true) {
                try {
                    airportTrafficController.landTrafficController.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (airportTrafficController.getDepartureQueue().size() < 2) {
                    try {
                        // below throws an exception when the queue is empty
                        Airplane airplaneToUndock = airportTrafficController.getUndockingQueue().remove();
                        String gatewayToUndock = airplaneToUndock.getAssignedGateway();
                        System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToUndock.getName() +
                                " has the permission to undock from gateway " + gatewayToUndock);
                        airplaneToUndock.performUndocking.start();
                        try {
                            airplaneToUndock.performUndocking.join();
                        } catch (InterruptedException e) {
                            System.out.println("Undocking operation interrupted unexpectedly.");
                            e.printStackTrace();
                            continue;
                        }
                        airportTrafficController.availableGateways.release();
                        airportTrafficController.gateways.offer(gatewayToUndock);
                        System.out.println(Thread.currentThread().getName() + " : Airplane " + airplaneToUndock.getName() +
                                " please proceed to join the departure queue beside the runway.");
                        airplaneToUndock.postUndockReply();
                        airplaneToUndock.endTimer();
                        airportTrafficController.addUndockingTime(airplaneToUndock.getElapsedTime());
                        airplaneToUndock.startTimer();
                        airportTrafficController.getDepartureQueue().add(airplaneToUndock);
                    } catch (NoSuchElementException ignored) {
                        // No airplanes waiting to undocked, thread can go back to sleep after this.
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " : Unable to undock any airplane due to full capacity in departure queue.");
                }
                airportTrafficController.landTrafficController.notify();
                if (airportTrafficController.isClosed() && airportTrafficController.getTotalAirplanesInPremise() == 0)
                    return;
            }
        }
    }
}
