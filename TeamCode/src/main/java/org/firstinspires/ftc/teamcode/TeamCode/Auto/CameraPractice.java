package org.firstinspires.ftc.teamcode.TeamCode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Cameras;
//import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "CameraPractice")
public class CameraPractice extends LinearOpMode {
    Cameras cams;
    //Turret turret;
    List<AprilTagDetection> currentDetections = new ArrayList<>();
    List<AprilTagDetection> lastDetections = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        cams = new Cameras(hardwareMap);
        //turret = new Turret(hardwareMap);

        telemetry.addData("Camera Initialized", cams.camInitialized);
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            currentDetections = cams.getDetections();
            if (!currentDetections.isEmpty()) {
                lastDetections = currentDetections;
            }

            telemetry.addLine("---Camera---");
            if (lastDetections.isEmpty()) {
                telemetry.addLine("No detections yet");
            } else {
                for (AprilTagDetection detection : lastDetections) {
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
                                    cams.aimTurret(clusterDet.metadata.name);
                                }
                            }
                        }
                    } else if (detection instanceof AprilTagSingleDetection) {
                        AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                        if (singleDet.metadata != null) {
                            telemetry.addLine("Tag Detected: " + singleDet.metadata.name + " (ID: " + singleDet.id + ")");
                            if (singleDet.ftcPose != null) {
                                telemetry.addLine("XYZ:" + singleDet.ftcPose.x + " " + singleDet.ftcPose.y + " " + singleDet.ftcPose.z);
                                telemetry.addLine("PRY:" + singleDet.ftcPose.pitch + " " + singleDet.ftcPose.roll + " " + singleDet.ftcPose.yaw);
                                telemetry.addLine("RBE:" + singleDet.ftcPose.range + " " + singleDet.ftcPose.bearing + " " + singleDet.ftcPose.elevation);
                            }
                        } else {
                            telemetry.addLine("Tag Detected ID: " + singleDet.id);
                        }
                    }
                }
            }
            telemetry.update();
        }

        if (cams != null) {
            cams.stop();
            telemetry.addLine("stopped");
        }
    }
}
