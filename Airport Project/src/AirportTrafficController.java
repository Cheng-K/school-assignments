import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AirportTrafficController extends Thread{

    private volatile boolean closed;
    private BlockingDeque<Airplane> landingQueue;
    private BlockingDeque<Airplane> dockingQueue;
    private BlockingQueue<Airplane> undockingQueue;
    private BlockingQueue<Airplane> departureQueue;
    public Semaphore availableGateways;
    public final ReentrantLock runway;
    public final ReentrantLock landTrafficController;
    private final Thread landingThread;
    private final Thread dockThread;
    private final Thread undockThread;
    private final Thread departureThread;
    private AtomicInteger totalAirplanesDeparted;
    private AtomicInteger totalAirplanesArrived;

    public AirportTrafficController() {
        runway = new ReentrantLock();
        availableGateways = new Semaphore(4);
        landTrafficController = new ReentrantLock();
        landingQueue = new LinkedBlockingDeque<>();
        dockingQueue = new LinkedBlockingDeque<>(2);
        undockingQueue = new LinkedBlockingQueue<>();
        departureQueue = new LinkedBlockingQueue<>(2);
        closed = false;

        landingThread = new Thread(new LandingCoordinator(this),"Air traffic controller");
        dockThread = new Thread(new DockingCoordinator(this),"Land traffic Controller");
        undockThread = new Thread(new UndockingCoordinator(this),"Land traffic Controller");
        departureThread = new Thread(new DepartureCoordinator(this),"Air traffic controller");

        // keep track of the airplanes entered and left the airport
        totalAirplanesDeparted = new AtomicInteger(0);
        totalAirplanesArrived = new AtomicInteger(0);
    }

    @Override
    public void run() {
        System.out.println("Starting airports");
        landingThread.start();
        dockThread.start();
        undockThread.start();
        departureThread.start();
        // Buffer so that all threads are in sleep condition before notifying
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (runway){
            runway.notifyAll();
        }
        synchronized (landTrafficController){
            landTrafficController.notifyAll();
        }
        try {
            landingThread.join();
            departureThread.join();
            dockThread.join();
            undockThread.join();
        } catch (InterruptedException e){
            System.out.println("Simulation program interrupted unexpectedly.");
            e.printStackTrace();
        }
        System.out.println("Simulation program terminated.");
    }

    public void addAirplaneToLandingQueue (Airplane airplane) {
        totalAirplanesArrived.incrementAndGet();
        landingQueue.add(airplane);
        Thread replyThread = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " : Airplane " + airplane.getName() + " please join the landing queue and fly in circles. Will come back to you when the runway is clear for you.");
        },"Airport Traffic Controller");
        replyThread.start();
        try{
            replyThread.join();
        } catch (InterruptedException e){
            System.out.println("Unexpected interruption occurred.");
            e.printStackTrace();
        }
    }

    public void addAirplaneToUndockQueue (Airplane airplane){
        undockingQueue.add(airplane);
        Thread replyThread = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " : added " + airplane.getName() + " into the waiting list. Please wait for further instructions to undock.");
        },"Airport Traffic Controller");
        replyThread.start();
        try{
            replyThread.join();
        } catch (InterruptedException e){
            System.out.println("Unexpected interruption occurred.");
            e.printStackTrace();
        }
    }

    /* Getters & Setters */
    
    public BlockingDeque<Airplane> getLandingQueue() {
        return landingQueue;
    }

    public BlockingDeque<Airplane> getDockingQueue() {
        return dockingQueue;
    }

    public BlockingQueue<Airplane> getUndockingQueue() {
        return undockingQueue;
    }

    public BlockingQueue<Airplane> getDepartureQueue() {
        return departureQueue;
    }

    public void setClosed (boolean value){
        closed = value;
    }

    public boolean isClosed () {
        return closed;
    }

    public int getTotalAirplanesInPremise () {
        return totalAirplanesArrived.intValue() - totalAirplanesDeparted.intValue();
    }

    public void incrementDepartedAirplane () {
        totalAirplanesDeparted.incrementAndGet();
    }


}
