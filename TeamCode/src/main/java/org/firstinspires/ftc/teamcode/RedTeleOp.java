package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;


@TeleOp(name = "Red TeleOp")
public class RedTeleOp extends OpMode {
    private Follower follower;

    private MecanumDriveTrain driveTrain;

    private Pose hivePose;

    private Intake intake;
    private Turret turret;
    private WebcamName webcam;
    private AprilTagProcessor aprilTag;
    private VisionPortal portal;


    private boolean isHiveFacingSouth;


    @Override
    public void init(){
        follower = Constants.createFollower(hardwareMap);
        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");
        driveTrain = new MecanumDriveTrain(hardwareMap);
        intake = new Intake(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry);
        isHiveFacingSouth = true;
        follower.startTeleOpDrive(true);
        //follower.setStartingPose(rx, ry, heading);

        aprilTag = new AprilTagProcessor.Builder()
                .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setLensIntrinsics(822.317, 822.317, 319.495, 242.502)
                .setNumThreads(3)
                .build();
        aprilTag.setDecimation(2);

        portal = new VisionPortal.Builder()
                .setCamera(webcam)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .addProcessor(aprilTag)
                .build();
    }

    @Override
    public void start(){}

    @Override
    public void loop(){
        //april tag detection
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections){
            if (detection.ftcPose != null){
                if (detection.id >= 30 && detection.id <= 33 && detection.ftcPose.pitch > 10){
                    isHiveFacingSouth = false;
                }
                if (detection.id >= 34 && detection.id <= 37 && detection.ftcPose.pitch > 10){
                    isHiveFacingSouth = true;
                }
                telemetry.addData("--- Tag ID", detection.id);
                if (detection.metadata != null) {
                    telemetry.addData("Name", detection.metadata.name);
                }
            }
        }

        if (isHiveFacingSouth){
            hivePose = new Pose(58, 58);
        }else{
            hivePose = new Pose(58, 84);
        }
        //controls
        if (gamepad1.right_bumper){
            intake.pullIn();
        }else if (gamepad1.right_trigger_pressed){
            intake.flowerIntake();
        }else {
            intake.stop();
        }

        if (gamepad1.left_bumper){
            turret.spinUp();
        }else if (gamepad1.left_trigger_pressed){
            turret.launch();
        }
        //button to reset pose to (0, 0) or some other coordinate

        //update methods
        follower.update();
        intake.update();
        turret.update(hivePose, follower.getPose(), intake);
        driveTrain.updateRelative(gamepad1, follower);

        //telemetry
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", follower.getPose().getHeading());
        telemetry.addData("Camera FPS", String.format(java.util.Locale.US, "%.2f", portal.getFps()));
        telemetry.addData("Camera State", portal.getCameraState());
        telemetry.addData("Is hive facing south", isHiveFacingSouth);
        telemetry.update();
    }
}
