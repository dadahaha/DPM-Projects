/*
 * Lab 1
 * Group 45
 * Niloofar Khoshsiyar 260515304
 * Sean Stappas 260639512
 */

public class PController extends UltrasonicController {
	private final int SPEEDCOEFFICIENT = 20;
	//Default Constructor
	public PController(int bandCenter, int bandwidth) {
		super(bandCenter, bandwidth, 100, 400);
	}
	
	/**
	 *  Moves left with a speed proportional to the distance from the band center.
	 *  Must be followed by updateSpeed() for the wheels' speed to be updated.
	 * @param diff The parameter determined by the distance to the wall.
	 */
	protected void moveLeft() {
		currentLeftSpeed = motorStraight - calcDiff(distError);
		currentRightSpeed = motorStraight + calcDiff(distError);
	}

	/**
	 *  Moves right with a speed proportional to the distance from the band center.
	 *  Must be followed by updateSpeed() for the wheels' speed to be updated.
	 * @param diff The parameter determined by the distance to the wall.
	 */
	protected void moveRight() {
		currentLeftSpeed = motorStraight + calcDiff(distError);
		currentRightSpeed = motorStraight - calcDiff(distError);
	}
	
	// Updates the speed of the wheels.
	@Override
	protected void updateSpeed() {
		// Controlling Speeds that might be too low
		if(currentLeftSpeed<motorLow)
			currentLeftSpeed=motorLow;
		if(currentRightSpeed<motorLow)
			currentRightSpeed=motorLow;

		// Controlling Speeds that might be too high
		if(currentLeftSpeed>motorHigh)
			currentLeftSpeed=motorHigh;
		if(currentRightSpeed>motorHigh)
			currentRightSpeed=motorHigh;

		leftMotor.setSpeed(currentLeftSpeed);      
		rightMotor.setSpeed(currentRightSpeed);	
	}


	/**
	 *  Calculates the speed difference based on the distance to the wall.
	 *  P-Type: Correction is proportional to error.
	 * @param diff The distance from the wall.
	 * @return The calculated speed difference.
	 */
	private int calcDiff (int diff) {
		return Math.abs(diff)*SPEEDCOEFFICIENT;
	}
}
