package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class turretOpModeAlign extends OpMode {
    private AprilTagWebcam  aprilTagWebcam = new AprilTagWebcam();
    private turretMechanism turret = new turretMechanism();
    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001};
    int stepIndex = 2;
    @Override
    public void init(){
        aprilTagWebcam.init(hardwareMap, telemetry);
        turret.init(hardwareMap);
        telemetry.addLine("Initialized All mechanisms");
    }
    public void start(){
        turret.resetTimer();
    }
    @Override
    public void loop(){
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificIds(20);
        turret.update(id20);

        if(gamepad1.bWasPressed()){
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if(gamepad1.dpadLeftWasPressed()){
            stepIndex = turret.setKp(turret.getKp() - stepSizes[stepIndex]);
        }
        if(gamepad1.dpadRightWasPressed()){
            stepIndex = turret.setKp(turret.getKp() + stepSizes[ stepIndex]);
        }
        if(gamepad1.dpadDownWasPressed()){
            stepIndex = turret.setKd(turret.getKd() - stepSizes[ stepIndex]);
        }
        if(gamepad1.dpadUpWasPressed()){
            stepIndex = turret.setKd(turret.getKd() + stepSizes[ stepIndex]);
        }
        if(id20 != null){
            telemetry.addData("currId: ", aprilTagWebcam);
        }
        else{
            telemetry.addLine("No tag detected");
        }
        telemetry.addLine("--------------------------------");
        telemetry.addData("Tuning P", "%.5f D-Pad Up/Down", turret.getKp());
        telemetry.addData("Tuning D", "%.5f D-Pad Left/Right", turret.getKd());
        telemetry.addData("Step Size", "%.5f B button", stepSizes[(int)stepIndex]);
    }

}
