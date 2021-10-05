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
                System.out.println(Thread.currentThread().getName()+ " : landing an airplane");
                // Wait for airplane to land
                // after that add airplane to docking queue
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+ " : Noted. Please exit the runway and join the docking queue to wait for instructions to dock at specific gateway.");
                // call post landing reply here for airplane
                airportTrafficController.runway.notify();
            }

        }
    }
}
