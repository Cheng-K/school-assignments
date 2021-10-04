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
                System.out.println(Thread.currentThread().getName() + " : coordinating departure of some airplanes");
                // Wait for airplane to finish departure
                // Remove the airplane from departure queue
                try {
                    Thread.sleep(3100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+ " : finished departure airplane");
                airportTrafficController.runway.notify();
            }
        }
    }
}
