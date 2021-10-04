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
                System.out.println(Thread.currentThread().getName() + " : is undocking some airplanes");
                // Wait for undocking to be done
                // Add airplane to departure queue
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+ " : finished undocking airplane");
                airportTrafficController.landTrafficController.notify();
            }
        }
    }
}
