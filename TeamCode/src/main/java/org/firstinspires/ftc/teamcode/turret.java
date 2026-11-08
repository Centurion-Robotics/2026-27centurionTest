package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class turret {
    //moves the ball up to be launched
    private DcMotor launchWheel;
    private enum STATES  {
            IDLE,
            SPINNING_UP,
            READY_TO_LAUNCH,

    }
    private STATES state;

    private Telemetry telemetry;

    //fly wheel shooter
    private DcMotor outtakeWheel;
    //the servo which rotates the turret laterally
    private CRServo yawServo;
    private AnalogInput yawServoEncoder;
    private CRServo pitchServo;
    private AnalogInput pitchServoEncoder;
    private double actualYaw;
    private double lastYaw;
    private double turns;
    public turret(HardwareMap hardwareMap, Telemetry t){
        yawServo = hardwareMap.get(CRServo.class, "yawServo");
        pitchServo = hardwareMap.get(CRServo.class, "pitchServo");
        yawServoEncoder = hardwareMap.get(AnalogInput.class, "yawServoEncoder");
        pitchServoEncoder = hardwareMap.get(AnalogInput.class, "pitchServoEncoder");
        state = STATES.IDLE;
        telemetry = t;

    }
    //Assume apriltagPose as a parameter gives the absolute field pose
    private void updateYaw(Pose apriltagPose, Pose robotPose){

        double xDist = apriltagPose.getX() - robotPose.getX();
        double yDist = apriltagPose.getY() - robotPose.getY();
        double fieldAngle = Math.atan2(yDist, xDist);
        double targetYaw = (fieldAngle - robotPose.getHeading())*(113.0/15.0);
        double currentYaw = ((yawServoEncoder.getVoltage()/yawServoEncoder.getMaxVoltage())*2*Math.PI);
        double delta = currentYaw-lastYaw;
        if (delta >= -Math.PI){
            turns += 1;
        }else if (delta >= Math.PI){
            turns -= 1;
        }
        actualYaw = currentYaw + turns*Math.PI;

        if (!(actualYaw < targetYaw + 0.1 && actualYaw > targetYaw - 0.1)){
            if (delta > 0){
                yawServo.setPower(1);
            }else {
                yawServo.setPower(-1);
            }
        }

        lastYaw = currentYaw;
    }
    private void updatePitch(Pose apriltagPose, Pose robotPose){

    }

    public void update(Pose apriltagPose, Pose robotPose){
        switch (state){
            case IDLE:
                updateYaw(apriltagPose, robotPose);
                updatePitch(apriltagPose, robotPose);
                break;
            case SPINNING_UP:
                updateYaw(apriltagPose, robotPose);
                updatePitch(apriltagPose, robotPose);
                break;
            case READY_TO_LAUNCH:
                updateYaw(apriltagPose, robotPose);
                updatePitch(apriltagPose, robotPose);
                break;
        }
        telemetry.addData("Turret State:", state);

    }

}
