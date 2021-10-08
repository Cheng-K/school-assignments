// Implement airplane generator && implement landing coordinator to test on landing part
import java.util.Random;

public class AirplaneGenerator extends Thread {
	int airplaneToBeGenerated;
	AirportTrafficController airportTrafficController;
	Random random = new Random(1234);

	public AirplaneGenerator(int numberOfAirplanes, AirportTrafficController airportTrafficController){
		airplaneToBeGenerated = numberOfAirplanes;
		this.airportTrafficController = airportTrafficController;
	}

	@Override
	public void run() {
		// Implement time interval for planes arriving
		for (int i = 0; i<airplaneToBeGenerated; i++){
			try {
				Thread.sleep(random.nextInt(6)*1000L);
			} catch (InterruptedException e){
				System.out.println("Airplane Generator interrupted unexpectedly.");
				e.printStackTrace();
			}
			new Airplane("A"+(i+1),airportTrafficController);
		}
		
		// signal closing time
		airportTrafficController.setClosed(true);
	}
}