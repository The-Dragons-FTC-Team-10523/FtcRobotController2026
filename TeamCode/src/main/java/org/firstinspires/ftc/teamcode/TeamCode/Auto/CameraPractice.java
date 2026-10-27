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
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;


@Autonomous(name = "CameraPractice")
public class CameraPractice extends LinearOpMode{
    Cameras cams;
    Turret turret;
    List<AprilTagDetection> AprilTagDetectionsPollen = cams.pollenCam.getDetections();

    @Override
    public void runOpMode() throws InterruptedException {
        cams = new Cameras(hardwareMap);
        turret = new Turret(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            for (AprilTagDetection detection : AprilTagDetectionsPollen) {
                telemetry.addLine("---Pollen Camera---");
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        telemetry.addLine("Cluster Detected:" + clusterDet.metadata.name + " " + clusterDet.percentClusterFound + "%");
                        telemetry.addLine("XYZ:" + clusterDet.ftcPose.x + " " + clusterDet.ftcPose.y + " " + clusterDet.ftcPose.z);
                        telemetry.addLine("PRY:" + clusterDet.ftcPose.pitch + " " + clusterDet.ftcPose.roll + " " + clusterDet.ftcPose.yaw);
                        telemetry.addLine("RBE:" + clusterDet.ftcPose.range + " " + clusterDet.ftcPose.bearing + " " + clusterDet.ftcPose.elevation);
                        if ( clusterDet.metadata.name == "RED AUDIENCE" || clusterDet.metadata.name == "RED SCORING") {//find actual apriltag names and change per teleop
                            if (-90 < clusterDet.ftcPose.roll && clusterDet.ftcPose.roll < 90) { //lmao what is this error T^T
                                telemetry.addLine("Correct Cell Aimed");
                                telemetry.update();
                                while (clusterDet.ftcPose.x > -5 || clusterDet.ftcPose.x < 5) {
                                    AprilTagDetectionsPollen = cams.pollenCam.getDetections(); //hopefully rechecking? might have to change a bit

                                    if (clusterDet.ftcPose.x > 0) {
                                        turret.pollenServos.setPower(.5); // moves right
                                    } else {
                                        turret.pollenServos.setPower(-.5); // moves left
                                    }
                                }
                                turret.pollenServos.setPower(0);
                                //flywheel shenanigans
                            }
                        }
                    }
                }
            }
            telemetry.update();
            // CAMERA 1 THAT'S CRAZY
            // CAMERA 2 THAT'S CRAZY

        }
    }
}
// to do- figure ou how to use the data to determine if