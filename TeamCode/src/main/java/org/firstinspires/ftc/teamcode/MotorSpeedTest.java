package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class MotorSpeedTest extends OpMode {

    private MecanumDriveTrain driveTrain;
    double motorSpeed = 0;
    boolean on = false;

    @Override
    public void init(){
        driveTrain = new MecanumDriveTrain(hardwareMap);



    }

    @Override
    public void loop(){

        if(gamepad1.aWasPressed()){
            motorSpeed += 0.01;
            telemetry.addData("Motor Speed: ", motorSpeed);
        }
        else if (gamepad1.bWasPressed()){
            motorSpeed -= 0.01;
            telemetry.addData("Motor Speed: ", motorSpeed);
        }

        else if (gamepad1.yWasPressed()){
            driveTrain.motorRun(0);
            on = !on;
            telemetry.addData("Power: ", on);
        }

        if(on){
            driveTrain.motorRun(motorSpeed);
        }

    }
}
