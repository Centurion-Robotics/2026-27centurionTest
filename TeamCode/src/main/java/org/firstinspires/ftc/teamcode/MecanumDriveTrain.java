package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDriveTrain {

    private DcMotor frontLeftWheel;
    private DcMotor frontRightWheel;
    private DcMotor rearLeftWheel;
    private DcMotor rearRightWheel;

    public MecanumDriveTrain(HardwareMap hardwareMap){
        frontRightWheel = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftWheel = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        rearRightWheel = hardwareMap.get(DcMotor.class, "rearRightMotor");
        rearLeftWheel = hardwareMap.get(DcMotor.class, "rearLeftMotor");

        frontLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        rearLeftWheel.setDirection(DcMotor.Direction.REVERSE);

        frontRightWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRightWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeftWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void update(Gamepad gamepad){
        double forward = gamepad.left_stick_y;
        double lateral = gamepad.left_stick_x;
        double rotation = gamepad.right_stick_x;

        double frontLeftPower = forward + lateral + rotation;
        double frontRightPower = forward - lateral - rotation;
        double rearLeftPower = forward - lateral + rotation;
        double rearRightPower = forward + lateral - rotation;

        double maxPower = 1.0;
        double maxSpeed = 0.5;

        maxPower = Math.max(maxPower, frontLeftPower);
        maxPower = Math.max(maxPower, frontRightPower);
        maxPower = Math.max(maxPower, rearRightPower);
        maxPower = Math.max(maxPower, rearLeftPower);

        frontLeftWheel.setPower((frontLeftPower/maxPower)*maxSpeed);
        frontRightWheel.setPower((frontRightPower/maxPower)*maxSpeed);
        rearRightWheel.setPower((rearRightPower/maxPower)*maxSpeed);
        rearLeftWheel.setPower((rearLeftPower/maxPower)*maxSpeed);
    }
}
