package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Darsh on 11/22/15.
 */
public class TankDriveClimberArm extends OpMode {
    /*
         * Note: the configuration of the servos is such that
         * as the climberServo servo approaches 0, the climberServo position moves up (away from the floor).
         * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
         */
    // TETRIX VALUES.
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    final static double CLAW_MIN_RANGE  = 0.20;
    final static double CLAW_MAX_RANGE  = 0.7;

    // position of the climberServo servo.
    double armPosition;

    // amount to change the climberServo servo position.
    double armDelta = 0.1;

    // position of the claw servo
    double clawPosition;

    // amount to change the claw servo position by
    double clawDelta = 0.1;

    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    //DcMotor middleHookWheel;
    Servo climber;
    Servo arm;
    int i = 0;

    public TankDriveClimberArm () {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the climberServo joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        backRightDrive = hardwareMap.dcMotor.get("backRightDrive");
        backLeftDrive = hardwareMap.dcMotor.get("backLeftDrive");
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive = hardwareMap.dcMotor.get("frontRightDrive");
        frontLeftDrive = hardwareMap.dcMotor.get("frontLeftDrive");
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        climber = hardwareMap.servo.get("climberServo");
        arm = hardwareMap.servo.get("armServo");
        //middleHookWheel = hardwareMap.dcMotor.get("middle");
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);*

		/*climberServo = hardwareMap.servo.get("servo_1");
		claw = hardwareMap.servo.get("servo_6");*/

        // assign the starting position of the wrist and claw
        armPosition = 0.2;
        clawPosition = 0.2;

    }

    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        frontRightDrive.setPower(right);
        frontLeftDrive.setPower(left);
        //middleHookWheel.setPower(right);
        backRightDrive.setPower(right);
        backLeftDrive.setPower(left);
        // middleHookWheel.setPower(right);

        // update the position of the climberServo.
        if (gamepad1.a) {
            // if the A button is pushed on gamepad1, increment the position of
            // the climberServo servo.
            climber.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad1.y) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the climberServo servo.
            climber.setPosition(Servo.MAX_POSITION);
        }

        if (gamepad1.x) {
            arm.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad1.b) {
            arm.setPosition(Servo.MAX_POSITION);
        }

        // update the position of the claw
        // clip the position values so that they never exceed their allowed range.
        //armPosition = Range.clip(armPosition, ARM_MIN_RANGE,›› ARM_MAX_RANGE);
        //clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        // write position values to the wrist and claw servo
        //climberServo.setPosition(armPosition);
        //claw.setPosition(clawPosition);
    }

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */

    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 0.85, 0.85 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
