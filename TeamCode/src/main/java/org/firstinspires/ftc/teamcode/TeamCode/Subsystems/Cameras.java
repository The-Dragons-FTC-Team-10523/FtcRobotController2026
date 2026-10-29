package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class Cameras {
    public AprilTagProcessor pollenCam;
    public AprilTagProcessor.Builder pollenCamBuilder;
    public VisionPortal pollenVisionPortal;

    public AprilTagProcessor nectarCam;
    public AprilTagProcessor.Builder nectarCamBuilder;
    public VisionPortal nectarVisionPortal;

    public boolean pollenCamInitialized;
    public boolean nectarCamInitialized;

    public Cameras(HardwareMap hardwareMap) {
        int[] viewIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.VERTICAL);

        // Initialize Pollen Camera
        try {
            pollenCamBuilder = new AprilTagProcessor.Builder();
            pollenCamBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
            pollenCamBuilder.setDrawTagID(true);
            pollenCamBuilder.setDrawAxes(true);
            pollenCamBuilder.setDrawTagOutline(true);
            pollenCam = pollenCamBuilder.build();

            pollenVisionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "pollenCam"))
                    .setLiveViewContainerId(viewIds[0])
                    .addProcessor(pollenCam)
                    .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                    .setAutoStopLiveView(true)
                    .build();

            pollenVisionPortal.setProcessorEnabled(pollenCam, true);
            pollenCamInitialized = true;
        } catch (Exception e) {
            pollenCamInitialized = false;
        }

        // Initialize Nectar Camera
        try {
            nectarCamBuilder = new AprilTagProcessor.Builder();
            nectarCamBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
            nectarCamBuilder.setDrawTagID(true);
            nectarCamBuilder.setDrawAxes(true);
            nectarCamBuilder.setDrawTagOutline(true);
            nectarCam = nectarCamBuilder.build();

            nectarVisionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "nectarCam"))
                    .setLiveViewContainerId(viewIds[1])
                    .addProcessor(nectarCam)
                    .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                    .setAutoStopLiveView(true)
                    .build();

            nectarVisionPortal.setProcessorEnabled(nectarCam, true);
            nectarCamInitialized = true;
        } catch (Exception e) {
            nectarCamInitialized = false;
        }
    }

    public List<AprilTagDetection> getPollenDetections() {
        if (pollenCamInitialized && pollenCam != null) {
            return pollenCam.getDetections();
        }
        return new ArrayList<>();
    }

    public List<AprilTagDetection> getNectarDetections() {
        if (nectarCamInitialized && nectarCam != null) {
            return nectarCam.getDetections();
        }
        return new ArrayList<>();
    }

    public void stop() {
        if (pollenVisionPortal != null) {
            try {
                pollenVisionPortal.close();
            } catch (Exception ignored) {}
        }
        if (nectarVisionPortal != null) {
            try {
                nectarVisionPortal.close();
            } catch (Exception ignored) {}
        }
    }
}
