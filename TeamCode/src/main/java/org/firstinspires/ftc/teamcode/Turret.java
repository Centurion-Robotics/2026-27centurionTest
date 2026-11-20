package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Config
public class Turret {
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
    private DcMotorEx flywheel;
    //the servo which rotates the turret laterally
    private CRServo yawServo;
    private AnalogInput yawServoEncoder;
    private CRServo pitchServo;
    private AnalogInput pitchServoEncoder;
    private ElapsedTime launchTimer;
    private double actualYaw;
    private double lastYaw;
    private double turns;

    //needs to be tuned

    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;

    public static double F = 0;

    private PIDController PID;
    private double flywheelVelocity;

    private VoltageSensor batteryVoltageSensor;
    public Turret(HardwareMap hardwareMap, Telemetry t){
        yawServo = hardwareMap.get(CRServo.class, "yawServo");
        pitchServo = hardwareMap.get(CRServo.class, "pitchServo");
        yawServoEncoder = hardwareMap.get(AnalogInput.class, "yawServoEncoder");
        pitchServoEncoder = hardwareMap.get(AnalogInput.class, "pitchServoEncoder");
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        launchServo = hardwareMap.get(Servo.class, "launchServo");
        flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        state = STATES.IDLE;
        telemetry = t;
        launchTimer = new ElapsedTime();
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        PID = new PIDController(Kp, Ki, Kd);
    }
    //Assume apriltagPose as a parameter gives the absolute field pose
    private void updateYaw(Pose cellPose, Pose robotPose){
        double xDist = cellPose.getX() - robotPose.getX();
        double yDist = cellPose.getY() - robotPose.getY();
        double fieldAngle = Math.atan2(yDist, xDist);
        double targetYaw = (fieldAngle - robotPose.getHeading())*(113.0/15.0);
        double currentYaw = ((yawServoEncoder.getVoltage()/yawServoEncoder.getMaxVoltage())*2*Math.PI);
        double delta = currentYaw-lastYaw;
        if (delta < -Math.PI){
            turns += 1;
        }else if (delta > Math.PI){
            turns -= 1;
        }
        actualYaw = currentYaw + turns*2*Math.PI;

        if (!(actualYaw < targetYaw + 0.1 && actualYaw > targetYaw - 0.1)){
            if (actualYaw < targetYaw){
                yawServo.setPower(1);
            }else {
                yawServo.setPower(-1);
            }
        } else {
            yawServo.setPower(0);
        }

        lastYaw = currentYaw;
    }


    private void updatePitch(Pose cellPose, Pose robotPose){

    }

    private void flywheelUpdate(){
        PID.setConstants(Kp, Ki, Kd);
        PID.update(flywheelVelocity - flywheel.getVelocity());
        double power = PID.getOutput() + F*(12/batteryVoltageSensor.getVoltage()) * flywheelVelocity;
        flywheel.setPower(power);
    }
    private void setTargetVelocity(double targetVelocity){
        if (flywheelVelocity != targetVelocity){
            PID.reset();
        }
        flywheelVelocity = targetVelocity;
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
                flywheelUpdate();
                setTargetVelocity(0);

                flywheel.setPower(0);
                launchServo.setPosition(0);
                break;
            case SPINNING_UP:
                double revolutionsPerSec = flywheel.getVelocity()/28;
                setTargetVelocity(1400);
                updateYaw(cellPose, robotPose);
                updatePitch(cellPose, robotPose);
                flywheelUpdate();

                launchServo.setPosition(0);

                telemetry.addData("Outtake velocity (Revolutions Per Sec)", revolutionsPerSec);

                if (revolutionsPerSec >= 50){
                    state = STATES.READY_TO_LAUNCH;
                }

                break;
            case READY_TO_LAUNCH:
                //turn on haptics
                setTargetVelocity(1400);
                updateYaw(cellPose, robotPose);
                updatePitch(cellPose, robotPose);
                flywheelUpdate();
                break;
            case LAUNCHING:
                intake.pullIn();
                flywheelUpdate();
                launchServo.setPosition(1);
                if (launchTimer.milliseconds()>1250){
                    state = STATES.IDLE;
                    launchServo.setPosition(0);
                    intake.stop();
                    setTargetVelocity(0);
                }
        }
        telemetry.addData("Turret State:", state);

    }

}
