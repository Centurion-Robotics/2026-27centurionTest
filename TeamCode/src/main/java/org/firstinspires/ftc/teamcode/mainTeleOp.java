package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="Main TeleOp")
@SuppressWarnings("unused")
public class MainTeleOp extends OpMode {
    private  MecanumDriveTrain driveTrain;
    private TeleOpLocalizer localizer;
    @Override
    public void init() {
        driveTrain = new MecanumDriveTrain(hardwareMap);
        localizer = new TeleOpLocalizer(hardwareMap);
    }

    @Override
    public void loop() {
        driveTrain.update(gamepad1);
        localizer.update();

        telemetry.addData("yaw:", localizer.yaw);
        telemetry.addData("pitch:", localizer.pitch);
        telemetry.addData("roll:", localizer.roll);

        telemetry.update();
    }
}
