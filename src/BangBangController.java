/*
 * Lab 1
 * Group 45
 * Niloofar Khoshsiyar 260515304
 * Sean Stappas 260639512
 */

public class BangBangController extends UltrasonicController{

	public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
		// Default Constructor
		super(bandCenter, bandwidth, motorLow, motorHigh);
	}
	
	/**
	 *  Moves left with a constant speed, no matter the distance from the band center.
	 *  Must be followed by updateSpeed() for the wheels' speed to be updated.
	 */
	@Override
	protected void moveLeft() {
		currentLeftSpeed=motorLow;
		currentRightSpeed=motorHigh;
	}
	
	/**
	 *  Moves right with a constant speed, no matter the distance from the band center
	 *  Must be followed by updateSpeed() for the wheels' speed to be updated.
	 */
	@Override
	protected void moveRight() {
		currentLeftSpeed=motorHigh;
		currentRightSpeed=motorLow;
	}
	
	/**
	 *  Updates the speed of the wheels.
	 */
	@Override
	protected void updateSpeed() {
		leftMotor.setSpeed(currentLeftSpeed);		     
		rightMotor.setSpeed(currentRightSpeed);	
	}
}
