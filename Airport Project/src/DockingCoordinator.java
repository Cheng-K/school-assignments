

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
                System.out.println(Thread.currentThread().getName() + " : is docking some airplanes");
                // Wait for airplane to finish dock
                // Trigger airplane to refill supplies and necessities
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+ " : finished docking airplane");
                airportTrafficController.landTrafficController.notify();
            }
        }
    }
}
