package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Main TeleOp")
@SuppressWarnings("unused")
public class mainTeleOp extends OpMode {
    private  MecanumDriveTrain driveTrain;
    private TeleOpLocalizer localizer;

    private Servo backdoorServo;


    private DcMotor intakeMotor;

    private DcMotor outtakeMotorTest;


    @Override
    public void init() {
        // Set up multiple telemetry (Driver Station + Dashboard)
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driveTrain = new MecanumDriveTrain(hardwareMap);
        localizer = new TeleOpLocalizer(hardwareMap);

        outtakeMotorTest = hardwareMap.get(DcMotor.class, "testOuttakeMotor");




        backdoorServo = hardwareMap.get(Servo.class, "backdoorServo");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
    }

    @Override
    public void loop() {
        driveTrain.updateRelative(gamepad1, localizer);
        localizer.update();

        if (gamepad1.a){
            backdoorServo.setPosition(0);

        }else{
            backdoorServo.setPosition(0.5);
        }

        if (gamepad1.b){
            outtakeMotorTest.setPower(-1);
        }else{
            outtakeMotorTest.setPower(0);
        }

        telemetry.addData("yaw:", localizer.yaw);
        telemetry.addData("pitch:", localizer.pitch);
        telemetry.addData("roll:", localizer.roll);
//        telemetry.addData("backdoorServo Position: ",  backdoorServo.getPosition());

        telemetry.update();
    }
}
