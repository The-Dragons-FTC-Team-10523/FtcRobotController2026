package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;


import java.util.ArrayList;
import java.util.List;

public class Cameras {

    public AprilTagProcessor cam;
    public VisionPortal visionPortal;

    //Turret turret;


    public boolean camInitialized;

    public Cameras(HardwareMap hardwareMap) {
        //turret = new Turret(hardwareMap);

        // Initialize  Camera
        try {
            cam = new AprilTagProcessor.Builder()
                    .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                    .setDrawTagID(true)
                    .setDrawAxes(true)
                    .setDrawTagOutline(true)
                    .build();
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "cam"))
                    .addProcessor(cam)
                    .setCameraResolution(new Size(640, 480))
                    .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                    .enableLiveView(true)
                    .build();
            camInitialized = true;
        } catch (Exception e) {
            camInitialized = false;
        }

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
        List<AprilTagDetection> detections = getDetections();
        for (AprilTagDetection detection : detections) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                if (clusterDet.metadata != null && metadata.equals(clusterDet.metadata.name)) {
                    if (clusterDet.ftcPose != null) {
                        if (clusterDet.ftcPose.x > .5) {
                            //turret.pollenServos.setPower(.5); // moves right
                        } else if (clusterDet.ftcPose.x < -.5) {
                            //turret.pollenServos.setPower(-.5); // moves left
                        } else {
                            //turret.pollenServos.setPower(0);
                        }
                    }
                }
            }
        }
    }
}
