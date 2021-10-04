import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class AirportTrafficController extends Thread{

    public volatile boolean isClosed;
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

    public AirportTrafficController() {
        runway = new ReentrantLock();
        availableGateways = new Semaphore(4);
        landTrafficController = new ReentrantLock();
        landingQueue = new LinkedBlockingDeque<>();
        dockingQueue = new LinkedBlockingDeque<>(2);
        undockingQueue = new LinkedBlockingQueue<>();
        departureQueue = new LinkedBlockingQueue<>(2);
        isClosed = false;

        landingThread = new Thread(new LandingCoordinator(this),"Air traffic controller");
        dockThread = new Thread(new DockingCoordinator(this),"Land Traffic Controller");
        undockThread = new Thread(new UndockingCoordinator(this),"Land Traffic Controller");
        departureThread = new Thread(new DepartureCoordinator(this),"Air traffic controller");
    }

    @Override
    public void run() {
        System.out.println("Starting airports");
        landingThread.start();
        dockThread.start();
        undockThread.start();
        departureThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (runway){
            runway.notifyAll();
        }
        synchronized (landTrafficController){
            landTrafficController.notifyAll();
        }
    }

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
}
