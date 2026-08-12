package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class TeleOpLocalizer {
    private IMU imu;
    public double yaw;
    public double pitch;
    public double roll;
    public  TeleOpLocalizer(HardwareMap hardwareMap){
        imu = hardwareMap.get(IMU.class, "imu");
    }
    public void update(){
        yaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        pitch = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.RADIANS);
        roll = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.RADIANS);
    }
}
