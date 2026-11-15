package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Turret {
    //moves the ball up to be launched
    private DcMotor launchWheel;
    private enum STATES  {
            IDLE,
            SPINNING_UP,
            READY_TO_LAUNCH,
            LAUNCHING,

    }
    private STATES state;

    private Telemetry telemetry;

    //fly wheel shooter
    private Servo launchServo;
    private DcMotorEx outtakeWheel;
    //the servo which rotates the turret laterally
    private CRServo yawServo;
    private AnalogInput yawServoEncoder;
    private CRServo pitchServo;
    private AnalogInput pitchServoEncoder;
    private ElapsedTime launchTimer;
    private double actualYaw;
    private double lastYaw;
    private double turns;

    public Turret(HardwareMap hardwareMap, Telemetry t){
        yawServo = hardwareMap.get(CRServo.class, "yawServo");
        pitchServo = hardwareMap.get(CRServo.class, "pitchServo");
        yawServoEncoder = hardwareMap.get(AnalogInput.class, "yawServoEncoder");
        pitchServoEncoder = hardwareMap.get(AnalogInput.class, "pitchServoEncoder");
        outtakeWheel = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
        outtakeWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        state = STATES.IDLE;
        telemetry = t;
        launchTimer = new ElapsedTime();
    }
    //Assume apriltagPose as a parameter gives the absolute field pose
    private void updateYaw(Pose cellPose, Pose robotPose){
        double xDist = cellPose.getX() - robotPose.getX();
        double yDist = cellPose.getY() - robotPose.getY();
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


    private void updatePitch(Pose cellPose, Pose robotPose){

    }

    public void spinUp(){
        state = STATES.SPINNING_UP;
    }
    public void launch(){
        if (state == STATES.READY_TO_LAUNCH){
            state = STATES.LAUNCHING;
            launchTimer.reset();
        }
    }
    public STATES getTurretState(){
        return state;
    }

    public void update(Pose cellPose, Pose robotPose, Intake intake){
        switch (state){
            case IDLE:
                updateYaw(cellPose, robotPose);
                updatePitch(cellPose, robotPose);

                outtakeWheel.setPower(0);
                launchServo.setPosition(0);
                break;
            case SPINNING_UP:
                double revolutionsPerSec = outtakeWheel.getVelocity()/28;
                updateYaw(cellPose, robotPose);
                updatePitch(cellPose, robotPose);

                outtakeWheel.setPower(1);
                launchServo.setPosition(0);

                telemetry.addData("Outtake velocity (Revolutons Per Sec)", revolutionsPerSec);

                if (revolutionsPerSec >= 75){
                    state = STATES.READY_TO_LAUNCH;
                }

                break;
            case READY_TO_LAUNCH:
                //turn on haptics
                updateYaw(cellPose, robotPose);
                updatePitch(cellPose, robotPose);
                break;
            case LAUNCHING:
                intake.pullIn();
                launchServo.setPosition(1);
                if (launchTimer.milliseconds()>1250){
                    state = STATES.IDLE;
                    launchServo.setPosition(0);
                    intake.stop();
                }
        }
        telemetry.addData("Turret State:", state);

    }

}
