package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.lang.Math.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ayylmao on 11/22/15.
 */
public class TankDriveClimberLinearAutoBlue extends LinearOpMode implements SensorEventListener{

    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor tape;
    DcMotor adjust;
    Servo deposit;
    //Servo climber;
    SensorManager mSensorManager;
    Sensor gyroscope;
    private static final float NS2S = 1.0f/1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private static final float EPSILON = 0.000001f;

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

    public void stopDrive(){
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void fullStop(){
        stopDrive();
        tape.setPower(0);
        adjust.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        backRightDrive = hardwareMap.dcMotor.get("backRightDrive");
        backLeftDrive = hardwareMap.dcMotor.get("backLeftDrive");
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive = hardwareMap.dcMotor.get("frontRightDrive");
        frontLeftDrive = hardwareMap.dcMotor.get("frontLeftDrive");
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        //climber = hardwareMap.servo.get("climber");
        tape = hardwareMap.dcMotor.get("tape");
        adjust = hardwareMap.dcMotor.get("adjust");
        deposit = hardwareMap.servo.get("deposit");
        //climber.setPosition(Servo.MIN_POSITION);
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        waitForStart();
        forward(.60);
        sleep(800);
        turnRight(.60);
        sleep(2000);
        forward(1);
        sleep(3000);
        stopDrive();

        //adjust.setPower(.4);
        //sleep(200);

//        tape.setPower(-.8);
//        sleep(500);
//        tape.setPower(0);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
// This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float)Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
