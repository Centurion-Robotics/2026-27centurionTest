package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class turretMechanism {
    private DcMotorEx turret;
    private double kP = 0.0001;
    private double kD = 0.000;
    private double goalX = 0;
    private double lastError = 0;
    private double angleTolerance = 0.2;
    private final double MAX_POWER = 0.6;
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();
    public void init(HardwareMap hwMap){
        turret = hwMap.get(DcMotorEx.class, "turret");
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void setKp(double newKp){
        kP = newKp;
    }
    public double getKp(){
        return kP;
    }
    public void setKd(double newKd){
        kD = newKd;
    }
    public double getKd(){
        return kD;
    }
    public void resetTimer(){
        timer.reset();
    }
    public void update(AprilTagDetection currId){
        double deltaTime = timer.seconds();
        timer.reset();

        if(currId == null){
            turret.setPower(0);
            lastError = 0;
            return;
        }
        double error = goalX - currId.ftcPose.bearing;
        double pTerm = error * kP;
        double dTerm = 0;
        if (deltaTime > 0 ){
            dTerm = (error - lastError)/deltaTime*kD;
        }
        if (Math.abs(error) < angleTolerance){
            power = 0;
        }
        else{
            power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
        }

        turret.setPower(power);
        lastError = error;
    }
}
