package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Config
public class MecanumDriveTrain {

    private DcMotor frontLeftWheel;
    private DcMotor frontRightWheel;
    private DcMotor rearLeftWheel;
    private DcMotor rearRightWheel;

    public static double MAX_SPEED = 1.0;

    public MecanumDriveTrain(HardwareMap hardwareMap){
        frontRightWheel = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftWheel = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        rearRightWheel = hardwareMap.get(DcMotor.class, "rearRightMotor");
        rearLeftWheel = hardwareMap.get(DcMotor.class, "rearLeftMotor");

        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        rearRightWheel.setDirection(DcMotor.Direction.REVERSE);

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

        maxPower = Math.max(maxPower, frontLeftPower);
        maxPower = Math.max(maxPower, frontRightPower);
        maxPower = Math.max(maxPower, rearRightPower);
        maxPower = Math.max(maxPower, rearLeftPower);

        frontLeftWheel.setPower((frontLeftPower/maxPower)*MAX_SPEED);
        frontRightWheel.setPower((frontRightPower/maxPower)*MAX_SPEED);
        rearRightWheel.setPower((rearRightPower/maxPower)*MAX_SPEED);
        rearLeftWheel.setPower((rearLeftPower/maxPower)*MAX_SPEED);
    }
}
