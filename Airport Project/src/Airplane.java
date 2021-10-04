// Airplane has four threads docking/undocking/arrival/departure/ therefore airplane is responsible for reporting for themselves

public class Airplane {

    /* Private classes encapsulated in the airplane class represent individual operations that an airplane can perform.*/

    private class Landing implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is preparing for landing.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " crashed unexpectedly.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " landed successfully");
        }
    }

    private class Departure implements Runnable {
        @Override
        public void run () {
            System.out.println(Thread.currentThread().getName() + " is preparing for takeoff.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName() + " departure process interrupted.");
            }
            System.out.println(Thread.currentThread().getName() + " departed successfully");
        }
    }

    private class Dock implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is preparing to dock at Gateway : ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " docking process interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " docked successfully at Gateway : ");
        }
    }

    private class UnloadAndLoad implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is letting passengers to disembark and unloading cargo.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " unloading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " passengers and cargo supplies successfully unloaded.");
            System.out.println(Thread.currentThread().getName() + " refilling supplies and letting passengers to board the airplane now.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " loading operations interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished loading all necessary supplies.");
        }
    }

    private class Undock implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is preparing to undock from Gateway : ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " undocking process interrupted.");
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " undocked successfully from Gateway : ");
        }
    }

    private final String name;
    private final AirportTrafficController controller;

    public final Thread performLanding;
    public final Thread performTakeOff;
    public final Thread performDocking;
    public final Thread performUndocking;
    public final Thread performUnloadAndLoad;

    public Airplane(String name,AirportTrafficController controller){
        this.name = name;
        this.controller = controller;
        performLanding = new Thread(new Landing(),name);
        performTakeOff = new Thread(new Departure(),name);
        performDocking = new Thread(new Dock(),name);
        performUndocking = new Thread(new Undock(),name);
        performUnloadAndLoad = new Thread(new UnloadAndLoad(),name);
        requestLanding();
    }

    private void requestLanding () {
        System.out.println(name + " : Approaching airport in 20 minutes. Requesting permission to land on runway.");
        // Plug in airport api here
    }

    // Might not be needed since undock -> straight go to takeoff queue ?
    private void requestTakeOff () {
        System.out.println(Thread.currentThread().getName() + " : Requesting permission to take off from airport.");
    }
}
