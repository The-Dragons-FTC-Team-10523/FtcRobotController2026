package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;

import java.util.ArrayList;
import java.util.List;

public class Cameras {

    public AprilTagProcessor cam;
    public AprilTagProcessor.Builder camBuilder;
    public VisionPortal visionPortal;
    Turret turret;


    public boolean camInitialized;

    public Cameras(HardwareMap hardwareMap) {
        turret = new Turret(hardwareMap);

        // Initialize  Camera
        try {
            camBuilder = new AprilTagProcessor.Builder();
            camBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
            camBuilder.setDrawTagID(true);
            camBuilder.setDrawAxes(true);
            camBuilder.setDrawTagOutline(true);
            cam = camBuilder.build();

            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "pollenCam"))
                    .addProcessor(cam)
                    .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                    .setAutoStopLiveView(true)
                    .build();

            visionPortal.setProcessorEnabled(cam, true);
            camInitialized = true;
        } catch (Exception e) {
            camInitialized = false;
        }

        // Initialize Nectar Camera

    }

    public List<AprilTagDetection> getDetections() {
        if (camInitialized && cam != null) {
            return cam.getDetections();
        }
        return new ArrayList<>();
    }


    public void stop() {
        if (visionPortal != null) {
            try {
                visionPortal.close();
            } catch (Exception ignored) {}
        }
    }
    public void aimTurret(String metadata){
        boolean isAimed = false;
        List<AprilTagDetection> detections = cam.getDetections();
        for (AprilTagDetection detection : detections) {
            AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
            if (clusterDet.metadata.name.equals(metadata)) {
                while(!isAimed) {
                    if (clusterDet.ftcPose.x > .5) {
                        turret.pollenServos.setPower(.5); // moves right
                    } else if (clusterDet.ftcPose.x < -.5) {
                        turret.pollenServos.setPower(-.5); // moves left
                    } else {
                        isAimed = true;
                        turret.pollenServos.setPower(0);
                    }
                }
            }
        }
    }
}
