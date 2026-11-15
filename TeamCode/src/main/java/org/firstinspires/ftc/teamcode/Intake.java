package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    private DcMotorEx intake;
    private Servo flowerServo;
    private enum STATES {
        INTAKING_IN,
        INTAKING_OUT,
        IDLE,
        FLOWER_INTAKING;
    }

    private STATES state;
    private Telemetry telemetry;
    //NOTE: the intake motor also drives the transfer
    public Intake(HardwareMap hardwareMap, Telemetry t) {
        telemetry = t;
        intake = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        flowerServo = hardwareMap.get(Servo.class, "flowerServo");
        state = STATES.IDLE;
    }
    public void update(){
        switch (state){
            case INTAKING_IN:
                intake.setPower(1);
                flowerServo.setPosition(0);
                break;
            case INTAKING_OUT:
                intake.setPower(-1);
                flowerServo.setPosition(0);
                break;
            case IDLE:
                intake.setPower(0);
                flowerServo.setPosition(0);
                break;
            case FLOWER_INTAKING:
                intake.setPower(1);
                flowerServo.setPosition(1);
        }
    }
    public void pullIn(){
        state = STATES.INTAKING_IN;
    }
    public void pullOut(){
        state = STATES.INTAKING_OUT;
    }
    public void flowerIntake(){
        state = STATES.FLOWER_INTAKING;
    }
    public void stop(){
        state = STATES.IDLE;
    }
    public STATES getIntakeState(){
        return state;
    }
}
