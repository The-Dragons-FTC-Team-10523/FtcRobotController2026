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
    public AprilTagProcessor nectarCam;
    public AprilTagProcessor.Builder pollenCamBuilder;
    public AprilTagProcessor.Builder nectarCamBuilder;
    public VisionPortal pollenVisionPortal;
    public VisionPortal nectarVisionPortal;
    public int[] wholeVisionPortal;
    public Cameras(HardwareMap hardwareMap) {
        wholeVisionPortal = VisionPortal.makeMultiPortalView(2, MultiPortalLayout.HORIZONTAL);
        pollenCamBuilder = new AprilTagProcessor.Builder();
        nectarCamBuilder = new AprilTagProcessor.Builder();
        pollenCamBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
        pollenCamBuilder.setDrawTagID(true);
        pollenCamBuilder.setDrawAxes(true);
        pollenCamBuilder.setDrawTagOutline(true);
        pollenCam = pollenCamBuilder.build();

        nectarCamBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
        nectarCamBuilder.setDrawTagID(true);
        nectarCamBuilder.setDrawAxes(true);
        nectarCamBuilder.setDrawTagOutline(true);
        nectarCam = nectarCamBuilder.build();

        pollenVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "pollenCam"))
                .addProcessor(pollenCam)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setLiveViewContainerId(wholeVisionPortal[0])
                .build();

        nectarVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "nectarCam"))
                .addProcessor(nectarCam)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setLiveViewContainerId(wholeVisionPortal[1])
                .build();
        //hopefully this works(i will play russian roulette if it doesn't)
        nectarVisionPortal.setProcessorEnabled(nectarCam, true);
        pollenVisionPortal.setProcessorEnabled(pollenCam, true);
    }

}
