/*
 * Lab 1
 * Group 45
 * Niloofar Khoshsiyar 260515304
 * Sean Stappas 260639512
 */

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicPoller extends Thread{
	private final NXTRegulatedMotor usMotor = Motor.B;
	private UltrasonicSensor us;
	private UltrasonicController cont;
	private int orientation = 1;
	private int cosAngle = 0;
	private int rotationCounter = 0;

	public UltrasonicPoller(UltrasonicSensor us, UltrasonicController cont) {
		this.us = us;
		this.cont = cont;
	}

	public void run() {
		while (true) {
			// If the sensor motor isn't moving
			if( !usMotor.isMoving() )
			{
				/* orientation = 1 : ultrasonic sensor facing left
				 * orientation = 0 : ultrasonic sensor at 45 degrees
				 * orientation = 1 : ultrasonic sensor facing forward
				 * The value of orientation shifts between these value according to a cosine function.
				 *  */
				orientation = (int) Math.cos(Math.toRadians(cosAngle));
				// Process the data received by the sensor at the position given by orientation
				cont.processUSData(orientation, us.getDistance());
				// Rotate the sensor motor accordingly:
				// If the sensor is at the 45 degree position...
				if(orientation==0) {
					// If rotationCounter is even
					if(rotationCounter%2==1)
						// Rotate the sensor 45 degrees clockwise
						usMotor.rotate(45, true);
					// If rotationCounter is odd
					else
						// Rotate the sensor 45 degrees counterclockwise
						usMotor.rotate(-45, true);
				}
				// If the sensor is either facing left or forward...
				else{
					// Rotate the sensor in the appropriate direction
					usMotor.rotate(45*orientation, true);
					// Increment rotationCounter so that the sensor motor rotates in the correct direction the next time orientation==0
					rotationCounter++;
				}
				// Increase cosAngle so that the value of orientation changes in the next iteration
				cosAngle += 90;
			}
			try { Thread.sleep(10); } catch(Exception e){} /* Sampling Rate */
			
			
		
		} 

	}

}
