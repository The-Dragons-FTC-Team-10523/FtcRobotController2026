package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.MultiPortalLayout;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Arrays;

public class Cameras {
    public AprilTagProcessor pollenCam;
    public AprilTagProcessor.Builder pollenCamBuilder;
    public VisionPortal pollenVisionPortal;

    public Cameras(HardwareMap hardwareMap) {
        pollenCamBuilder = new AprilTagProcessor.Builder();
        pollenCamBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
        pollenCamBuilder.setDrawTagID(true);
        pollenCamBuilder.setDrawAxes(true);
        pollenCamBuilder.setDrawTagOutline(true);
        pollenCam = pollenCamBuilder.build();

        pollenVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "pollenCam"))
                .addProcessor(pollenCam)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setAutoStopLiveView(true)//maybe?
                .build();

        pollenVisionPortal.setProcessorEnabled(pollenCam, true);
    }

}
