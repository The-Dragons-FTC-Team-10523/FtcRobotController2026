package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.MultiPortalLayout;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Cameras {
    public AprilTagProcessor swyftCameraOne;
    public AprilTagProcessor swyftCameraTwo;
    public AprilTagProcessor.Builder swyftCameraOneBuilder;
    public AprilTagProcessor.Builder swyftCameraTwoBuilder;
    public VisionPortal swyftVisionPortalOne;
    public VisionPortal swyftVisionPortalTwo;
    public int[] wholeVisionPortal;
    public Cameras(HardwareMap hardwareMap) {
       swyftCameraOneBuilder = new AprilTagProcessor.Builder();
       swyftCameraTwoBuilder = new AprilTagProcessor.Builder();
       swyftCameraOneBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
       swyftCameraOneBuilder.setDrawTagID(true);
       swyftCameraOneBuilder.setDrawAxes(true);
       swyftCameraOneBuilder.setDrawTagOutline(true);
       swyftCameraOne = swyftCameraOneBuilder.build();
       //two????
        swyftCameraTwoBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
        swyftCameraTwoBuilder.setDrawTagID(true);
        swyftCameraTwoBuilder.setDrawAxes(true);
        swyftCameraTwoBuilder.setDrawTagOutline(true);
        swyftCameraTwo = swyftCameraTwoBuilder.build();
       swyftVisionPortalOne = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "swyftCameraOne"))
                .addProcessor(swyftCameraOne)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setAutoStopLiveView(true)
               .setLiveViewContainerId(0)
                .build();
       swyftVisionPortalTwo = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "swyftCameraTwo"))
                .addProcessor(swyftCameraTwo)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .setAutoStopLiveView(true)
               .setLiveViewContainerId(1)
                .build();
       wholeVisionPortal = VisionPortal.makeMultiPortalView(2, MultiPortalLayout.VERTICAL);
      //I think this is all I need to do?
       //IM TRYING MY BEST OKAY
    }

}
