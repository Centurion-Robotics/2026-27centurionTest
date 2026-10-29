package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "turretTrackingTest")
public class turretTrackingTest extends OpMode {
    private WebcamName webCam;
    private AprilTagProcessor aprilTag;
    private VisionPortal portal;

    @Override
    public void init(){
        webCam = hardwareMap.get(WebcamName.class, "WebCam 1");
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        portal = new VisionPortal.Builder()
                .setCamera(webCam)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .addProcessor(aprilTag)
                .build();
    }

    @Override
    public void loop(){
        telemetry.addData("Camera FPS", String.format(java.util.Locale.US, "%.2f", portal.getFps()));
        telemetry.addData("Camera State", portal.getCameraState());

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections){
            if (detection.metadata != null){
                telemetry.addData("Tag " + detection.id, detection.metadata.name);
            }
        }
        telemetry.update();
    }
}
