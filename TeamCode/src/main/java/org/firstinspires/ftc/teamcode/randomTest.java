package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "manmohitSmellsLikeFeetTeleOp")
public class randomTest extends OpMode {
    CRServo pitchServo;
    DcMotorEx outtakeMotor;
    CRServo yawServo;
    public void init(){
        pitchServo = hardwareMap.get(CRServo.class, "pitchServo");
        yawServo = hardwareMap.get(CRServo.class, "yawServo");
        outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
    }
    public void loop(){
        if (gamepad1.dpad_up){
            pitchServo.setPower(-1);
        }else if (gamepad1.dpad_down){
            pitchServo.setPower(1);
        }
        if (gamepad1.dpad_left){
            yawServo.setPower(1);
        }else if (gamepad1.dpad_right){
            yawServo.setPower(-1);
        }else{
            yawServo.setPower(0);
        }

        if (gamepad1.a){
            outtakeMotor.setPower(1);
        }else{
            outtakeMotor.setPower(0);
        }

    }
}
