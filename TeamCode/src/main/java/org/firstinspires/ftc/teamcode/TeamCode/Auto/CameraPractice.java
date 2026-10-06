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
        while (opModeIsActive()) {
            for (AprilTagDetection detection : AprilTagDetections1) {
                telemetry.addLine("---Camera One---");
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        telemetry.addLine("Cluster Detected:" + clusterDet.metadata.name + " " + clusterDet.percentClusterFound + "%");
                        telemetry.addLine("XYZ:" + clusterDet.ftcPose.x + " " + clusterDet.ftcPose.y + " " + clusterDet.ftcPose.z);
                        telemetry.addLine("PRY:" + clusterDet.ftcPose.pitch + " " + clusterDet.ftcPose.roll + " " + clusterDet.ftcPose.yaw);
                        telemetry.addLine("RBE:" + clusterDet.ftcPose.range + " " + clusterDet.ftcPose.bearing + " " + clusterDet.ftcPose.elevation);
                    }
                }
            }
            for (AprilTagDetection detection : AprilTagDetections2) {
                telemetry.addLine("---Camera Two---");
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        telemetry.addLine("Cluster Detected:" + clusterDet.metadata.name + " " + clusterDet.percentClusterFound + "%");
                        telemetry.addLine("XYZ:" + clusterDet.ftcPose.x + " " + clusterDet.ftcPose.y + " " + clusterDet.ftcPose.z);
                        telemetry.addLine("PRY:" + clusterDet.ftcPose.pitch + " " + clusterDet.ftcPose.roll + " " + clusterDet.ftcPose.yaw);
                        telemetry.addLine("RBE:" + clusterDet.ftcPose.range + " " + clusterDet.ftcPose.bearing + " " + clusterDet.ftcPose.elevation);
                    }
                }
            }
            telemetry.update();
            // CAMERA 1 THATS CRAZY
            for (AprilTagDetection detection : AprilTagDetections1){
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        if ( clusterDet.metadata.name == "RED AUDIENCE" || clusterDet.metadata.name == "RED SCORING" ){//find actual apriltag names and change per teleop
                            if ( -90 < clusterDet.ftcPose.roll && clusterDet.ftcPose.roll < 90) { //lmao what is this error T^T
                                telemetry.addLine("Correct Cell Aimed");
                                telemetry.update();
                                while (clusterDet.ftcPose.x > -5 && clusterDet.ftcPose.y < 5){
                                    if (clusterDet.ftcPose.x > 0){
                                        // the turret moves right
                                    } else{
                                        // the turret moves left
                                    }
                                }
                                //flywheel shenanigans
                            }
                        }
                    }
                }
            }
            // CAMERA 2 THATS CRAZY
            for (AprilTagDetection detection : AprilTagDetections1){
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    if (clusterDet.metadata != null) {
                        if ( clusterDet.metadata.name == "RED AUDIENCE" || clusterDet.metadata.name == "RED SCORING" ){//find actual apriltag names and change per teleop
                            if ( -90 < clusterDet.ftcPose.roll && clusterDet.ftcPose.roll < 90) { //lmao what is this error T^T
                                telemetry.addLine("Correct Cell Aimed");
                                telemetry.update();
                                while (clusterDet.ftcPose.x > -5 && clusterDet.ftcPose.y < 5){
                                    if (clusterDet.ftcPose.x > 0){
                                        // the turret moves right
                                    } else{
                                        // the turret moves left
                                    }
                                }
                                //flywheel shenanigans
                            }
                        }
                    }
                }
            }
        }
    }
}
// to do- figure ou how to use the data to determine if
//update the sdk !!! --fixed, was a gradle sync issue (its always gradle...)