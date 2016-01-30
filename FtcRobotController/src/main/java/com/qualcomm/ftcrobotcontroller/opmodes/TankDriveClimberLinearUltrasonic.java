package com.qualcomm.ftcrobotcontroller.opmodes;


import android.hardware.Sensor;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.util.UnknownFormatConversionException;

/**
 * Created by ayylmao on 11/22/15.
 */
public class TankDriveClimberLinearUltrasonic extends LinearOpMode {

    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor tape;
    DcMotor adjust;
    Servo deposit;
    LegacyModule legacyModule;
    UltrasonicSensor ultraLeft;
    UltrasonicSensor ultraRight;
    public void forward(double val) {
        frontLeftDrive.setPower(val);
        frontRightDrive.setPower(val);
        backLeftDrive.setPower(val);
        backRightDrive.setPower(val);
    }
    public void backwards(double val) {
        frontLeftDrive.setPower(-val);
        frontRightDrive.setPower(-val);
        backLeftDrive.setPower(-val);
        backRightDrive.setPower(-val);
    }
    public void turnLeft(double val) {
        frontLeftDrive.setPower(-val);
        frontRightDrive.setPower(val);
        backLeftDrive.setPower(-val);
        backRightDrive.setPower(val);
    }
    public void turnRight(double val) {
        frontLeftDrive.setPower(val);
        frontRightDrive.setPower(-val);
        backLeftDrive.setPower(val);
        backRightDrive.setPower(-val);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        backRightDrive = hardwareMap.dcMotor.get("backRightDrive");
        backLeftDrive = hardwareMap.dcMotor.get("backLeftDrive");
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive = hardwareMap.dcMotor.get("frontRightDrive");
        frontLeftDrive = hardwareMap.dcMotor.get("frontLeftDrive");
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        tape = hardwareMap.dcMotor.get("tape");
        adjust = hardwareMap.dcMotor.get("adjust");
        deposit = hardwareMap.servo.get("deposit");
        legacyModule = hardwareMap.legacyModule.get("legacy");
        ultraLeft = hardwareMap.ultrasonicSensor.get("ultraLeft");
        ultraRight = hardwareMap.ultrasonicSensor.get("ultraRight");
        waitForStart();
        while(true) {
            sleep(500);
            telemetry.addData("ultraL", "Left: " + ultraLeft.getUltrasonicLevel());
            telemetry.addData("ultraR", "Right: " + ultraRight.getUltrasonicLevel());
        }

//        forward(.60);
//        sleep(1000);
//        turnLeft(.60);
//        sleep(400);
//        forward(.60);
//        sleep(1000);
    }
}
