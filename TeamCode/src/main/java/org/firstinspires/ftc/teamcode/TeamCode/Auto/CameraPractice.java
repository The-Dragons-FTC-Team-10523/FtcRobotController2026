package org.firstinspires.ftc.teamcode.TeamCode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Cameras;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;


@Autonomous(name = "CameraPractice")
public class CameraPractice extends LinearOpMode{
    Cameras cams;
    List<AprilTagDetection> AprilTagDetections1 = cams.swyftCameraOne.getDetections();
    List<AprilTagDetection> AprilTagDetections2 = cams.swyftCameraTwo.getDetections();

    @Override
    public void runOpMode() throws InterruptedException {
        cams = new Cameras(hardwareMap);

        waitForStart();

        for(AprilTagDetection detection : AprilTagDetections1 ) {
            if (detection instanceof AprilTagClusterDetection){

       }
          }
        for(AprilTagDetection detection : AprilTagDetections2 ) {
            if (detection instanceof AprilTagClusterDetection){

          }
      }
    }
}
// https://javadoc.io/doc/org.firstinspires.ftc/Vision/latest/index.html
//update the sdk !!! --fixed, was a gradle sync issue (its always gradle...)