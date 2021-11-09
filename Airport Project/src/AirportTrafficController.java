import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/* Class name :  Airport Traffic Controller
   Description : Control Panel for landing/departure/docking/undocking coordinator. Contains all relevant information about the airport.
 */

public class AirportTrafficController extends Thread {

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
    private long[] minTotalMaxLandingTime;
    private long[] minTotalMaxDockingTime;
    private long[] minTotalMaxUndockingTime;
    private long[] minTotalMaxTakeoffTime;
    private int totalPassengersArrived;
    private int totalPassengersDeparted;

    public AirportTrafficController() {
        runway = new ReentrantLock();
        availableGateways = new Semaphore(4);
        landTrafficController = new ReentrantLock();
        landingQueue = new LinkedBlockingDeque<>();
        dockingQueue = new LinkedBlockingDeque<>(2);
        undockingQueue = new LinkedBlockingQueue<>();
        departureQueue = new LinkedBlockingQueue<>(2);
        gateways = new ArrayBlockingQueue<>(4, false, Arrays.asList("1", "2", "3", "4")); // set fairness to false (default)
        closed = false;

        landingThread = new Thread(new LandingCoordinator(this), "Air traffic controller");
        dockThread = new Thread(new DockingCoordinator(this), "Land traffic Controller");
        undockThread = new Thread(new UndockingCoordinator(this), "Land traffic Controller");
        departureThread = new Thread(new DepartureCoordinator(this), "Air traffic controller");

        // keep track of the airplanes entered and left the airport
        totalAirplanesDeparted = new AtomicInteger(0);
        totalAirplanesArrived = new AtomicInteger(0);

        minTotalMaxLandingTime = new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE};
        minTotalMaxDockingTime = new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE};
        minTotalMaxUndockingTime = new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE};
        minTotalMaxTakeoffTime = new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE};

    }

    /*
    Method name : run (Method to be called when thread is started)
    Parameter   : Null
    Description : Initialize all the four coordinators to start and make sure all four of them finished their operation before closing airport.
    Return      : Null
    */
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
        synchronized (runway) {
            runway.notifyAll();
        }
        synchronized (landTrafficController) {
            landTrafficController.notifyAll();
        }
        try {
            landingThread.join();
            departureThread.join();
            dockThread.join();
            undockThread.join();
        } catch (InterruptedException e) {
            System.out.println("Simulation program interrupted unexpectedly.");
            e.printStackTrace();
        }
        System.out.println("Simulation program terminated.");
    }


    public void addAirplaneToLandingQueue(Airplane airplane) {
        String replyString;
        if (airplane.requireEmergencyAttention())
            replyString = " : Airplane " + airplane.getName() + " has the highest priority to land. Please join the landing queue and fly in circles. Will allow you to land as soon as possible once the runway is clear.";
        else
            replyString = " : Airplane " + airplane.getName() + " please join the landing queue and fly in circles. Will come back to you when the runway is clear for you.";

        Thread replyThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + replyString);
        }, "Airport Traffic Controller");
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unexpected interruption occurred.");
            e.printStackTrace();
        }
        totalAirplanesArrived.incrementAndGet();
        if (airplane.requireEmergencyAttention())
            landingQueue.addFirst(airplane);
        else
            landingQueue.add(airplane);
    }

    public void addAirplaneToUndockQueue(Airplane airplane) {
        Thread replyThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " : added " + airplane.getName() + " into the waiting list. Please wait for further instructions to undock.");
        }, "Airport Traffic Controller");
        replyThread.start();
        try {
            replyThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unexpected interruption occurred.");
            e.printStackTrace();
        }
        undockingQueue.add(airplane);
    }

    /*
       Method name : generateReport
       Parameter   : Null
       Description : Generate statistics report at the end of simulation program (average waiting time, number of passengers served)
       Return      : None
     */

    public void generateReport() {
        System.out.println("Total number of airplanes : " + totalAirplanesArrived.get());
        System.out.println("Total number of passengers arrived : " + totalPassengersArrived);
        System.out.println("Total number of passengers departed : " + totalPassengersDeparted);
        if (availableGateways.tryAcquire(4)) {
            System.out.println("All gateways are empty");
            availableGateways.release(4);
        } else {
            System.out.println("Some gateways are still occupied !! ");
        }
        System.out.println("\n--------- Landing ---------");
        System.out.println("Minimum time taken for airplane to wait and complete landing : " + minTotalMaxLandingTime[0]);
        System.out.println("Average time taken for airplane to wait and complete landing : " + minTotalMaxLandingTime[1] / totalAirplanesArrived.get());
        System.out.println("Maximum time taken for airplane to wait and complete landing : " + minTotalMaxLandingTime[2]);
        System.out.println("\n--------- Docking ---------");
        System.out.println("Minimum time taken for airplane to wait and complete docking : " + minTotalMaxDockingTime[0]);
        System.out.println("Average time taken for airplane to wait and complete docking : " + minTotalMaxDockingTime[1] / totalAirplanesArrived.get());
        System.out.println("Maximum time taken for airplane to wait and complete docking : " + minTotalMaxDockingTime[2]);
        System.out.println("\n--------- Undocking ---------");
        System.out.println("Minimum time taken for airplane to wait and complete undocking : " + minTotalMaxUndockingTime[0]);
        System.out.println("Average time taken for airplane to wait and complete undocking : " + minTotalMaxUndockingTime[1] / totalAirplanesArrived.get());
        System.out.println("Maximum time taken for airplane to wait and complete undocking : " + minTotalMaxUndockingTime[2]);
        System.out.println("\n--------- Takeoff ---------");
        System.out.println("Minimum time taken for airplane to wait and complete takeoff : " + minTotalMaxTakeoffTime[0]);
        System.out.println("Average time taken for airplane to wait and complete takeoff : " + minTotalMaxTakeoffTime[1] / totalAirplanesArrived.get());
        System.out.println("Maximum time taken for airplane to wait and complete takeoff : " + minTotalMaxTakeoffTime[2]);
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

    public void setClosed(boolean value) {
        closed = value;
    }

    public boolean isClosed() {
        return closed;
    }

    /*
       Method name : getTotalAirplanesInPremise
       Parameter   : Null
       Description : Get the total number of airplanes that are going/still to be in the airport
       Return      : Integer
     */

    public int getTotalAirplanesInPremise() {
        return totalAirplanesArrived.intValue() - totalAirplanesDeparted.intValue();
    }

    public void addPassengersArrived(int arrivedPassengers) {
        totalPassengersArrived += arrivedPassengers;
    }

    public void addPassengersDeparted(int departedPassengers){
        totalPassengersDeparted += departedPassengers;
    }

    /*
        Method name : addLandingTime
        Parameter   : landing time to be added
        Description : add landing time taken by a newly arrived airplane into the array & update the min/max value (if necessary)
        Return      : Null
   */
    public void addLandingTime(long newLandingTime) {
        if (newLandingTime < minTotalMaxLandingTime[0])
            minTotalMaxLandingTime[0] = newLandingTime;
        if (newLandingTime > minTotalMaxLandingTime[2])
            minTotalMaxLandingTime[2] = newLandingTime;
        minTotalMaxLandingTime[1] += newLandingTime;
    }

    /*
    Method name : addDockingTime
    Parameter   : docking time to be added
    Description : add docking time taken by a newly docked airplane into the array & update the min/max value (if necessary)
    Return      : Null
    */
    public void addDockingTime(long newDockingTime) {
        if (newDockingTime < minTotalMaxDockingTime[0])
            minTotalMaxDockingTime[0] = newDockingTime;
        if (newDockingTime > minTotalMaxDockingTime[2])
            minTotalMaxDockingTime[2] = newDockingTime;
        minTotalMaxDockingTime[1] += newDockingTime;
    }

    /*
    Method name : addUndockingTime
    Parameter   : undocking time to be added
    Description : add undocking time taken by a newly undocked airplane into the array & update the min/max value (if necessary)
    Return      : Null
    */
    public void addUndockingTime(long newUndockingTime) {
        if (newUndockingTime < minTotalMaxUndockingTime[0])
            minTotalMaxUndockingTime[0] = newUndockingTime;
        if (newUndockingTime > minTotalMaxUndockingTime[2])
            minTotalMaxUndockingTime[2] = newUndockingTime;
        minTotalMaxUndockingTime[1] += newUndockingTime;
    }

    /*
        Method name : addTakeoffTime
        Parameter   : takeoff time to be added
        Description : add departure time taken by a newly departed airplane into the array & update the min/max value (if necessary)
        Return      : Null
   */

    public void addTakeoffTime(long newTakeoffTime) {
        if (newTakeoffTime < minTotalMaxTakeoffTime[0])
            minTotalMaxTakeoffTime[0] = newTakeoffTime;
        if (newTakeoffTime > minTotalMaxTakeoffTime[2])
            minTotalMaxTakeoffTime[2] = newTakeoffTime;
        minTotalMaxTakeoffTime[1] += newTakeoffTime;
    }

    public void incrementDepartedAirplane() {
        totalAirplanesDeparted.incrementAndGet();
    }

}
