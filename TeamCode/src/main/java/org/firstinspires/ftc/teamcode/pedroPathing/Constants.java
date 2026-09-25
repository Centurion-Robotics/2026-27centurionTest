package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Constants and configurations for Pedro Pathing follower using
 * Mecanum drivetrain and goBILDa Pinpoint localizer.
 */
@Configurable
public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();
    public static MecanumConstants mecanumConstants = new MecanumConstants();
    public static PinpointConstants pinpointConstants = new PinpointConstants();

    // Default Path Constraints (maxVelocity, maxAcceleration, maxAngularVelocity, maxAngularAcceleration)
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    static {
        // Drivetrain Motor Names matching hardware map
        mecanumConstants.leftFrontMotorName = "frontLeftMotor";
        mecanumConstants.leftRearMotorName = "rearLeftMotor";
        mecanumConstants.rightFrontMotorName = "frontRightMotor";
        mecanumConstants.rightRearMotorName = "rearRightMotor";

        // Drivetrain Motor Directions matching robot hardware configuration
        mecanumConstants.leftFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        mecanumConstants.leftRearMotorDirection = DcMotorSimple.Direction.FORWARD;
        mecanumConstants.rightFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        mecanumConstants.rightRearMotorDirection = DcMotorSimple.Direction.REVERSE;

        mecanumConstants.yVelocity = 80.858483;
        mecanumConstants.xVelocity = 48.534274;

        // Pinpoint Localizer hardware map name and pod offsets (adjust as needed)
        pinpointConstants.hardwareMapName = "pinpoint";
        pinpointConstants.forwardPodY = 1.0;  // Y Offset of Forward Encoder from center (inches)
        pinpointConstants.strafePodX = -2.5; // X Offset of Strafe Encoder from center (inches)

    }

    /**
     * Creates and builds a Follower instance configured with Mecanum drivetrain and Pinpoint localizer.
     *
     * @param hardwareMap FTC HardwareMap
     * @return Configured Follower instance
     */
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(mecanumConstants)
                .pinpointLocalizer(pinpointConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
