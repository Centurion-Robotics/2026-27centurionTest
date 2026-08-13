package org.firstinspires.ftc.teamcode;


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


    @Override
    public void init() {
        driveTrain = new MecanumDriveTrain(hardwareMap);
        localizer = new TeleOpLocalizer(hardwareMap);

        backdoorServo = hardwareMap.get(Servo.class, "backdoorServo");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
    }

    @Override
    public void loop() {
        driveTrain.update(gamepad1);
        localizer.update();

        if (gamepad1.a){
            backdoorServo.setPosition(0);

        }else{
            backdoorServo.setPosition(0.5);
        }

        if (gamepad1.b){
            intakeMotor.setPower(0.5);
        }else{
            intakeMotor.setPower(0);
        }

        telemetry.addData("yaw:", localizer.yaw);
        telemetry.addData("pitch:", localizer.pitch);
        telemetry.addData("roll:", localizer.roll);
        telemetry.addData("backdoorServo Position: ",  backdoorServo.getPosition());

        telemetry.update();
    }
}
