package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleOpLocalizer {
    private GoBildaPinpointDriver pinpoint;
    public double yaw;
    public double pitch;
    public double roll;

    public TeleOpLocalizer(HardwareMap hardwareMap){
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(0, 0, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        pinpoint.resetPosAndIMU();
    }

    public void update(){
        pinpoint.update();
        yaw = pinpoint.getHeading(AngleUnit.RADIANS);
        pitch = pinpoint.getPitch(AngleUnit.RADIANS);
        roll = pinpoint.getRoll(AngleUnit.RADIANS);
    }
}
