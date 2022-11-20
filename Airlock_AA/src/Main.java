import airlock.entities.*;
import airlock.exceptions.AirLockException;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			double exteriorPressure = 1.0;
			double lockPressure = 1.0;
			double interiorPressure = 1.0;
			
			IPressureSensor exteriorSensor = new PressureSensor(exteriorPressure);
			IPressureSensor lockSensor     = new PressureSensor(lockPressure);
			IPressureSensor interiorSensor = new PressureSensor(interiorPressure);
			
			IDoor exteriorDoor = new Door(exteriorSensor, lockSensor, DoorState.CLOSED);
			IDoor interiorDoor = new Door(interiorSensor, lockSensor, DoorState.CLOSED);
			
			IAirLock airLock = new AirLock(exteriorDoor, interiorDoor, lockSensor);
			
			String menuFormatString = 
			"""
			
			%s
			Exterior %s 
			Lock %s 
			Interior %s
			Exterior %s, 
			Interior %s
			
			Select option:
			    OX - open external door
			    OI - open internal door
	
			    CX - close external door
			    CI - close internal door
	
			    SX - set external pressure
			    SI - set internal pressure
			    
			    EX - equalise lock with external pressure
			    EI - equalise lock with internal pressure
			    
			    TM - toggle operation mode
			    
			    Q  - quit
			
			Selection: """;
			
			Scanner scanner = new Scanner(System.in);
			//testLoop
			boolean finished = false;
			
			while (!finished) {		
				String ans = null;
				
				try {
					System.out.printf(menuFormatString,
						airLock,
						exteriorSensor, lockSensor, interiorSensor,
						exteriorDoor, interiorDoor);
					
					ans = scanner.nextLine();
					
					switch (ans.toUpperCase().strip()) {
					
						case "OX" :					
							airLock.openOuterDoor();
							break;
							
						case "OI" :
							airLock.openInnerDoor();
							break;
							
						case "CX" :
							airLock.closeOuterDoor();
							break;
							
						case "CI" :
							airLock.closeInnerDoor();
							break;
							
						case "SX" :
							System.out.println("Enter external pressure: ");
							ans = scanner.nextLine();
							double exP = Double.valueOf(ans).doubleValue();
							exteriorSensor.setPressure(exP);
							if (exteriorDoor.isOpen()) {
								lockSensor.setPressure(exP);
								if (interiorDoor.isOpen()) {
									interiorSensor.setPressure(exP);
								}
							}
							break;
						
						case "SI" :
							System.out.println("Enter internal pressure: ");
							ans = scanner.nextLine();
							double inP = Double.valueOf(ans).doubleValue();
							interiorSensor.setPressure(inP);
							if (interiorDoor.isOpen()) {
								lockSensor.setPressure(inP);
								if (exteriorDoor.isOpen()) {
									exteriorSensor.setPressure(inP);
								}
							}
							break;
							
						case "EX" :
							airLock.equaliseWithEnvironmentPressure();
							break;
	
						case "EI" :
							airLock.equaliseWithCabinPressure();
							break;
	
						case "TM" :
							System.out.println("Toggle operation mode");
							airLock.toggleOperationMode();
							break;
							
						case "Q" :
							finished = true;
							break;						
							
						default:
							System.out.printf("Unrecognised option: %s\n", ans);
					}		
				}
				catch (AirLockException e) {
					System.err.println(e.getMessage());
				}
			} //while
			scanner.close();
		}
		catch (Exception e) {
			System.err.println("General error: " + e.getMessage());				
		}
		System.out.println("\nExiting\n");
	}

}
