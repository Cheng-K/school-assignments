import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AirportTrafficController extends Thread{

    private volatile boolean closed;
    private BlockingDeque<Airplane> landingQueue;
    private BlockingDeque<Airplane> dockingQueue;
    private BlockingQueue<Airplane> undockingQueue;
    private BlockingQueue<Airplane> departureQueue;
    public final BlockingQueue<String> gateways;
    public Semaphore availableGateways;
    public final ReentrantLock runway;
    public final ReentrantLock landTrafficController;
    private final Thread landingThread;
    private final Thread dockThread;
    private final Thread undockThread;
    private final Thread departureThread;
    private AtomicInteger totalAirplanesDeparted;
    private AtomicInteger totalAirplanesArrived;
    private long totalLandingTime;
    private long totalDockingTime;
    private long totalUndockingTime;
    private long totalTakeoffTime;

    public AirportTrafficController() {
        runway = new ReentrantLock();
        availableGateways = new Semaphore(4);
        landTrafficController = new ReentrantLock();
        landingQueue = new LinkedBlockingDeque<>();
        dockingQueue = new LinkedBlockingDeque<>(2);
        undockingQueue = new LinkedBlockingQueue<>();
        departureQueue = new LinkedBlockingQueue<>(2);
        gateways = new ArrayBlockingQueue<>(4,false, Arrays.asList("1","2","3","4")); // set fairness to false (default)
        closed = false;

        landingThread = new Thread(new LandingCoordinator(this),"Air traffic controller");
        dockThread = new Thread(new DockingCoordinator(this),"Land traffic Controller");
        undockThread = new Thread(new UndockingCoordinator(this),"Land traffic Controller");
        departureThread = new Thread(new DepartureCoordinator(this),"Air traffic controller");

        // keep track of the airplanes entered and left the airport
        totalAirplanesDeparted = new AtomicInteger(0);
        totalAirplanesArrived = new AtomicInteger(0);

        totalLandingTime = 0;
        totalDockingTime = 0;
        totalUndockingTime = 0;
        totalTakeoffTime = 0;
    }

    @Override
    public void run() {
        System.out.println("Starting airports");
        landingThread.start();
        dockThread.start();
        undockThread.start();
        departureThread.start();
        // Buffer to make sure all threads started in sleep condition before notifying
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
        String replyString;
        if (airplane.requireEmergencyAttention())
            replyString = " : Airplane " + airplane.getName() + " has the highest priority to land. Please join the landing queue and fly in circles. Will allow you to land as soon as possible once the runway is clear.";
        else
            replyString = " : Airplane " + airplane.getName() + " please join the landing queue and fly in circles. Will come back to you when the runway is clear for you.";

        Thread replyThread = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + replyString);
        },"Airport Traffic Controller");
        replyThread.start();
        try{
            replyThread.join();
        } catch (InterruptedException e){
            System.out.println("Unexpected interruption occurred.");
            e.printStackTrace();
        }
        totalAirplanesArrived.incrementAndGet();
        if (airplane.requireEmergencyAttention())
            landingQueue.addFirst(airplane);
        else
            landingQueue.add(airplane);
    }

    public void addAirplaneToUndockQueue (Airplane airplane){
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
        undockingQueue.add(airplane);
    }

    public void generateReport () {
        System.out.println("Total time taken for airplane to wait and complete landing : " + totalLandingTime);
        System.out.println("Total time taken for airplane to wait and complete docking : " + totalDockingTime);
        System.out.println("Total time taken for airplane to wait and complete undocking : " + totalUndockingTime);
        System.out.println("Total time taken for airplane to wait and complete takeoff : " + totalTakeoffTime);
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

    public long getTotalLandingTime () {
        return totalLandingTime;
    }

    public void setTotalLandingTime(long totalLandingTime) {
        this.totalLandingTime = totalLandingTime;
    }

    public long getTotalDockingTime() {
        return totalDockingTime;
    }

    public void setTotalDockingTime(long totalDockingTime) {
        this.totalDockingTime = totalDockingTime;
    }

    public long getTotalUndockingTime() {
        return totalUndockingTime;
    }

    public void setTotalUndockingTime(long totalUndockingTime) {
        this.totalUndockingTime = totalUndockingTime;
    }

    public long getTotalTakeoffTime() {
        return totalTakeoffTime;
    }

    public void setTotalTakeoffTime(long totalTakeoffTime) {
        this.totalTakeoffTime = totalTakeoffTime;
    }

    public void incrementDepartedAirplane () {
        totalAirplanesDeparted.incrementAndGet();
    }



}
