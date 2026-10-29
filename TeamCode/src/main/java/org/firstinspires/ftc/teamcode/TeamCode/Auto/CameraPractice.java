package org.firstinspires.ftc.teamcode.TeamCode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Cameras;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "CameraPractice")
public class CameraPractice extends LinearOpMode {
    Cameras cams;
    Turret turret;
    List<AprilTagDetection> AprilTagDetectionsPollen = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        cams = new Cameras(hardwareMap);
        turret = new Turret(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            AprilTagDetectionsPollen = cams.getPollenDetections();
            for (AprilTagDetection detection : AprilTagDetectionsPollen) {
                telemetry.addLine("---Pollen Camera---");
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        telemetry.addLine("Cluster Detected:" + clusterDet.metadata.name + " " + clusterDet.percentClusterFound + "%");
                        if (clusterDet.ftcPose != null) {
                            telemetry.addLine("XYZ:" + clusterDet.ftcPose.x + " " + clusterDet.ftcPose.y + " " + clusterDet.ftcPose.z);
                            telemetry.addLine("PRY:" + clusterDet.ftcPose.pitch + " " + clusterDet.ftcPose.roll + " " + clusterDet.ftcPose.yaw);
                            telemetry.addLine("RBE:" + clusterDet.ftcPose.range + " " + clusterDet.ftcPose.bearing + " " + clusterDet.ftcPose.elevation);
                        }
                        if ("RED AUDIENCE".equals(clusterDet.metadata.name) || "RED SCORING".equals(clusterDet.metadata.name)) {
                            if (clusterDet.ftcPose != null && -90 < clusterDet.ftcPose.roll && clusterDet.ftcPose.roll < 90) {
                                telemetry.addLine("Correct Cell Aimed");
                                telemetry.update();
                                while (opModeIsActive() && clusterDet.ftcPose != null && (clusterDet.ftcPose.x > -5 || clusterDet.ftcPose.x < 5)) {
                                    AprilTagDetectionsPollen = cams.getPollenDetections();

                                    if (clusterDet.ftcPose.x > 0) {
                                        turret.pollenServos.setPower(.5); // moves right
                                    } else {
                                        turret.pollenServos.setPower(-.5); // moves left
                                    }
                                }
                                turret.pollenServos.setPower(0);
                                //flywheel shenanigans T^T
                            }
                        }
                    }
                }
            }
            telemetry.update();
        }

        if (cams != null) {
            cams.stop();
        }
    }
}
