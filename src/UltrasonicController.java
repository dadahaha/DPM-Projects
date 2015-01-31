/*
 * Lab 1
 * Group 45
 * Niloofar Khoshsiyar 260515304
 * Sean Stappas 260639512
 */

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public abstract class UltrasonicController {
	protected final int bandCenter, bandwidth, frontDist=25;
	protected final int motorLow, motorHigh;
	protected final int motorStraight = 200;
	protected final int GAPFILTER = 2;
	protected final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	protected final int LEFT=1, DIAGONAL=0, FWD=-1;
	protected int distance;
	protected int currentLeftSpeed;
	protected int currentRightSpeed;
	protected int gapCheck = 0;
	protected int gapThreshold = 100;
	
	private boolean isTurningLeft;
	private boolean isTurningRight;
	private final int maxTurnCounter = 3;
	private int turnCounter;

	protected int distError;
	
	// Constructor. Initializes the variables.
	public UltrasonicController (int bandCenter, int bandwidth, int motorLow, int motorHigh) {
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		currentRightSpeed = 0;
		isTurningLeft = false;
		isTurningRight = false;
		turnCounter = 0;
	}

	/**
	 * Process a movement based on the us distance passed in (BANG-BANG style)
	 */
	public void processUSData(int orientation, int distance){
		this.distance = distance;
		
		
		if( orientation==FWD){
			// If the NXT is turning right and is far enough from the wall OR it has turned enough to reach maxTurnCounter 
			if ( (isTurningRight && distance >= frontDist) || turnCounter>=maxTurnCounter ) {
				// Go straight
				isTurningRight = false;
				turnCounter = 0;
				rightMotor.forward();
				moveStraight();
			}
			// If the NXT is turning right and is too close to the wall
			else if( isTurningRight && distance < frontDist ) {
				// Turn right
				moveRight();
				turnCounter++;
			}
			// If there's a wall close in front of the NXT
			else if(distance<frontDist){
				// Turn right
				isTurningRight = true;
				// Make the right wheel move backwards, allowing for a smoother turn
				rightMotor.backward();
				moveRight();
			}
			else{
				// Go straight
				moveStraight();
			}
		}
		
		
		// Sensor is to the left or at an angle of 45 degrees
		else if( orientation==LEFT || orientation==DIAGONAL){
			// If the sensor is at an angle
			if(orientation==DIAGONAL)
				// Take the distance component perpendicular to the wall
				distance=(int)(Math.cos(Math.toRadians(45))*distance);
			distError = distance-bandCenter;
			
			// If the NXT is turning left and is close enough to the wall OR it has turned enough to reach maxTurnCounter 
			if ( (isTurningLeft && distance <= bandCenter + bandwidth) || turnCounter>=maxTurnCounter ) {
				// Go straight
				isTurningLeft = false;
				turnCounter = 0;
				moveStraight();
			}
			// Else if the NXT is turning left and is too far from the wall
			else if( isTurningLeft && distance > bandCenter + bandwidth ) {
				// Turn left
				moveLeft();
				turnCounter++;
			}
			// Else if gapCheck has reached the value of GAPFILTER (it's not a gap, the robot has to turn left)
			else if( gapCheck == GAPFILTER ) {
				// Reset gapCheck
				gapCheck = 0;
				// Turn left
				isTurningLeft = true;
				moveLeft();
			}
			else {
				// If there's a gap to the left (potential convex turn)
				if (distance > gapThreshold && currentLeftSpeed==currentRightSpeed) {
					// Increment gapCheck and go straight
					gapCheck++;
					moveStraight();
				}
				// Else if there's a wall to the left
				else {
					gapCheck = 0;
					// Case 1:  Error in bounds
					if (Math.abs(distError) <= bandwidth) {
						moveStraight();
					}
					// Case 2: Negative error, moving too close to wall 
					else if (distError < 0) {
						moveRight();
					}
					// Case 3: Positive error, moving too far from wall
					else if (distError > 0) {
						moveLeft();
					}
				}
			}
		}
		updateSpeed();
	}
	
	/**
	 *  Abstract method to update the speed of the wheels.
	 */
	protected abstract void updateSpeed();

	/**
	 *  Moves the robot to left. This method is overwritten by Bang bang controller.
	 */
	protected void moveLeft(){}
	
	/**
	 *  Moves the robot to right. This method is overwritten by Bang bang controller.
	 */
	protected void moveRight(){}
	
	/**
	 *  Moves the robot straight.
	 */
	protected void moveStraight() {
		currentLeftSpeed=motorStraight;
		currentRightSpeed=motorStraight;
	}

	/**
	 * Turns right instantaneously when there is a close wall in front.
	 * Rotates about the center axis of the robot.
	 */
	protected void turnRight() {
		currentLeftSpeed=motorStraight;
		rightMotor.backward();
		currentRightSpeed=motorStraight;
		updateSpeed();
		rightMotor.forward();
	}

	/**
	 * Turns left instantaneously when there is no wall in the left.
	 * Rotates about the center axis of the robot.
	 */
	protected void turnLeft() {
		currentRightSpeed=motorStraight;
		leftMotor.backward();
		currentLeftSpeed=motorStraight;
		updateSpeed();
		leftMotor.forward();
	}	
	
	/**
	 * Returns the distance measured by the sensor
	 * @return The distance from the wall.
	 */
	public int readUSDistance(){
		return this.distance;
	}
}
