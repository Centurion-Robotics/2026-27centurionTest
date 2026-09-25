package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();
    public static MecanumConstants mecanumConstants = new MecanumConstants();
    public static PinpointConstants pinpointConstants = new PinpointConstants();

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    static {
        // Follower Constants
        followerConstants.mass = 12.5; // TODO: Weigh robot in kg!

        // Mecanum Drivetrain Constants
        mecanumConstants.leftFrontMotorName = "frontLeftMotor";
        mecanumConstants.rightFrontMotorName = "frontRightMotor";
        mecanumConstants.leftRearMotorName = "rearLeftMotor";
        mecanumConstants.rightRearMotorName = "rearRightMotor";

        mecanumConstants.leftFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        mecanumConstants.rightFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        mecanumConstants.leftRearMotorDirection = DcMotorSimple.Direction.FORWARD;
        mecanumConstants.rightRearMotorDirection = DcMotorSimple.Direction.REVERSE;

        mecanumConstants.yVelocity = 80.858483;
        mecanumConstants.xVelocity = 48.534274;
        mecanumConstants.useVoltageCompensation(true);



        // Pinpoint Localizer Constants
        pinpointConstants.hardwareMapName = "pinpoint";
        pinpointConstants.forwardPodY = 0; // TODO: Set your Pinpoint Y offset
        pinpointConstants.strafePodX = 0; // TODO: Set your Pinpoint X offset
    }

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(mecanumConstants)
                .pinpointLocalizer(pinpointConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
