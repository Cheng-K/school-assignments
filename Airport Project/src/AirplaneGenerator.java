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

	/*
    Method name : run (Method to be called when thread is started)
    Parameter   : Null
    Description : Instantiate airplanes with a random interval between 0-3 seconds. Once done, set the airport status to closed.
    Return      : Null
    */
	@Override
	public void run() {
		for (int i = 0; i<airplaneToBeGenerated; i++){
			try {
				Thread.sleep(random.nextInt(3)*1000L);
			} catch (InterruptedException e){
				System.out.println("Airplane Generator interrupted unexpectedly.");
				e.printStackTrace();
			}
			if (i%4 == 0)
				new Airplane("A"+(i+1),airportTrafficController,true);
			else
				new Airplane("A"+(i+1),airportTrafficController,false);
		}
		
		// signal the airport that all planes arrived and should be closing
		airportTrafficController.setClosed(true);
	}
}