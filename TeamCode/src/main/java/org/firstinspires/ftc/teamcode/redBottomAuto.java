package org.firstinspires.ftc.teamcode;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.function.Supplier;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Blue Bottom")
public class redBottomAuto extends OpMode {

    private Timer opmodeTimer;
    private final Pose startPose = new Pose(56, 8, 90);
    private final Pose shootPose = new Pose(59.7408810572687, 36.31167400881058, 90);

    public enum STATES {
            INITLAUNCH,


    }

    private STATES currentState = STATES.INITLAUNCH;
    private Follower follower;

    private PathChain toInitialLaunch;

    private void buildPaths(){
        toInitialLaunch = follower.pathBuilder().addPath(
                new BezierCurve(
                        startPose,
                        shootPose
                )
        ).setConstantHeadingInterpolation(Math.toRadians(180)).build();
    }

    private void updateState(){
        switch(currentState){
            case INITLAUNCH:
                follower.followPath(toInitialLaunch);
                if (!follower.isBusy()){

                }
        }
    }


    public void init(){
        opmodeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        follower.setPose(startPose);
        buildPaths();
    }
    public void start(){
        opmodeTimer.resetTimer();

    }
    public void loop(){
        follower.update();
        updateState();
        telemetry.addData("Time elapsed", opmodeTimer);
        telemetry.update();
    }

}
