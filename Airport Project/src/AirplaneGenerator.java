import java.util.Random;


/*
	Class Name  : AirplaneGenerator
	Description : A thread that will generate a specified number of airplanes for simulation program.
				  The airplanes will arrive with a random time interval between 0-5 seconds.
*/
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
		for (int i = 0; i<airplaneToBeGenerated; i++){
			try {
				Thread.sleep(random.nextInt(6)*1000L); 
			} catch (InterruptedException e){
				System.out.println("Airplane Generator interrupted unexpectedly.");
				e.printStackTrace();
			}
			new Airplane("A"+(i+1),airportTrafficController);
		}
		
		// signal the airport that all planes arrived and should be closing
		airportTrafficController.setClosed(true);
	}
}