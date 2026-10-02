package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import java.util.List;

/**
 * LimelightIntakeHelper
 * 
 * Subsystem helper for retrieving Limelight 3A vision detections (Detector,
 * Classifier, or standard pipeline), calculating target distance, and
 * computing driving commands to approach and intake targets.
 */
public class LimelightIntakeHelper {

    private Limelight3A limelight;
    private boolean initializedSuccessfully = false;

    // Physical mounting configuration (in inches and degrees)
    private double cameraHeightInches = 8.0;   // Lens center height above ground
    private double cameraPitchDegrees = 15.0;  // Upward (+)/downward (-) pitch angle relative to horizontal
    private double targetHeightInches = 1.5;   // Target ball center height above ground

    // Control gains for tracking
    private double kpTurn = 0.035;     // Proportional gain for steering towards target tx
    private double kpDrive = 0.05;     // Proportional gain for forward drive
    private double minDrivePower = 0.15; // Minimum drive power to overcome mechanical friction

    public static class TargetData {
        public boolean hasTarget = false;
        public String className = "None";
        public double tx = 0.0;           // Horizontal angle error (degrees)
        public double ty = 0.0;           // Vertical angle error (degrees)
        public double area = 0.0;         // Target area (% of image)
        public double confidence = 0.0;   // Neural Network detection confidence
        public double distanceInches = 0.0; // Estimated ground distance
        public double turnPower = 0.0;    // Drivetrain turn power
        public double drivePower = 0.0;   // Drivetrain forward drive power
        public double strafePower = 0.0;  // Drivetrain strafe power
        
        // Diagnostic info
        public boolean resultValid = false;
        public int detectorCount = 0;
        public int classifierCount = 0;
        public int pipelineIndex = 0;
        public String statusText = "";
    }

    public LimelightIntakeHelper(HardwareMap hardwareMap, String limelightHardwareName) {
        try {
            limelight = hardwareMap.get(Limelight3A.class, limelightHardwareName);
            limelight.pipelineSwitch(0);
            limelight.start();
            initializedSuccessfully = true;
        } catch (Exception e) {
            initializedSuccessfully = false;
        }
    }

    public void setPipeline(int index) {
        if (limelight != null) {
            limelight.pipelineSwitch(index);
        }
    }

    public void setCameraMounting(double heightInches, double pitchDegrees, double targetHeightInches) {
        this.cameraHeightInches = heightInches;
        this.cameraPitchDegrees = pitchDegrees;
        this.targetHeightInches = targetHeightInches;
    }

    public void setGains(double kpTurn, double kpDrive, double minDrivePower) {
        this.kpTurn = kpTurn;
        this.kpDrive = kpDrive;
        this.minDrivePower = minDrivePower;
    }

    /**
     * Polls the Limelight for detections across Detector, Classifier, and Primary target results.
     *
     * @param targetLabel Desired class label (e.g., "nectar" or "pollen"). Leave empty "" to accept any target.
     * @param targetStopDistanceInches Distance in inches to stop in front of target.
     */
    public TargetData getTargetData(String targetLabel, double targetStopDistanceInches) {
        TargetData data = new TargetData();

        if (!initializedSuccessfully || limelight == null) {
            data.statusText = "Limelight Hardware Not Found in Config!";
            return data;
        }

        LLStatus status = limelight.getStatus();
        if (status != null) {
            data.pipelineIndex = status.getPipelineIndex();
        }

        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            data.statusText = "No valid LLResult received (check pipeline & USB cable)";
            return data;
        }

        data.resultValid = true;

        List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
        List<LLResultTypes.ClassifierResult> classifierResults = result.getClassifierResults();

        data.detectorCount = (detectorResults != null) ? detectorResults.size() : 0;
        data.classifierCount = (classifierResults != null) ? classifierResults.size() : 0;

        String searchLabel = (targetLabel != null) ? targetLabel.trim().toLowerCase() : "";

        LLResultTypes.DetectorResult firstDetector = null;

        // 1. Search Detector Results (Neural Network Bounding Boxes)
        if (detectorResults != null && !detectorResults.isEmpty()) {
            double highestConfidence = -1.0;
            for (LLResultTypes.DetectorResult dr : detectorResults) {
                if (firstDetector == null) firstDetector = dr;
                String label = dr.getClassName().toLowerCase();
                if (searchLabel.isEmpty() || label.contains(searchLabel)) {
                    if (dr.getConfidence() > highestConfidence) {
                        highestConfidence = dr.getConfidence();
                        data.hasTarget = true;
                        data.className = dr.getClassName();
                        data.tx = dr.getTargetXDegrees();
                        data.ty = dr.getTargetYDegrees();
                        data.area = dr.getTargetArea();
                        data.confidence = dr.getConfidence();
                    }
                }
            }
        }

        // 2. Search Classifier Results if no specific Detector match found
        if (!data.hasTarget && classifierResults != null && !classifierResults.isEmpty()) {
            double highestConfidence = -1.0;
            for (LLResultTypes.ClassifierResult cr : classifierResults) {
                String label = cr.getClassName().toLowerCase();
                if (searchLabel.isEmpty() || label.contains(searchLabel)) {
                    if (cr.getConfidence() > highestConfidence) {
                        highestConfidence = cr.getConfidence();
                        data.hasTarget = true;
                        data.className = cr.getClassName();
                        data.tx = result.getTx();
                        data.ty = result.getTy();
                        data.area = result.getTa();
                        data.confidence = cr.getConfidence();
                    }
                }
            }
        }

        // 3. Fallback A: Use the first available detector if a specific label didn't match
        if (!data.hasTarget && firstDetector != null) {
            data.hasTarget = true;
            data.className = firstDetector.getClassName();
            data.tx = firstDetector.getTargetXDegrees();
            data.ty = firstDetector.getTargetYDegrees();
            data.area = firstDetector.getTargetArea();
            data.confidence = firstDetector.getConfidence();
        }

        // 4. Fallback B: Main Primary Target (tx/ty/ta)
        if (!data.hasTarget && (result.getTa() > 0.01 || Math.abs(result.getTx()) > 0.01 || Math.abs(result.getTy()) > 0.01)) {
            data.hasTarget = true;
            data.className = searchLabel.isEmpty() ? "Target" : searchLabel;
            data.tx = result.getTx();
            data.ty = result.getTy();
            data.area = result.getTa();
            data.confidence = 1.0;
        }

        if (data.hasTarget) {
            data.statusText = "Target Locked: " + data.className;

            // Distance estimation: d = (h2 - h1) / tan(cameraPitch + ty)
            double totalAngleRad = Math.toRadians(cameraPitchDegrees + data.ty);
            double tanVal = Math.tan(totalAngleRad);
            if (Math.abs(tanVal) > 0.001) {
                data.distanceInches = Math.abs((targetHeightInches - cameraHeightInches) / tanVal);
            } else {
                data.distanceInches = 12.0; // Default reasonable fallback distance
            }

            // Calculate steering turn power
            data.turnPower = Range.clip(data.tx * kpTurn, -0.6, 0.6);

            // Calculate forward drive power (always positive towards ball when tracking)
            if (data.area < 15.0) { // Drive forward until object fills ~15% of screen
                data.drivePower = 0.35; // Steady, controlled approach speed
            } else {
                data.drivePower = 0.15; // Slow down when right in front of intake
            }
        } else {
            data.statusText = "Searching for '" + (searchLabel.isEmpty() ? "any target" : searchLabel) + "'...";
        }

        return data;
    }

    public boolean isInitialized() {
        return initializedSuccessfully;
    }

    public Limelight3A getLimelight() {
        return limelight;
    }

    public void stop() {
        if (limelight != null) {
            try {
                limelight.stop();
            } catch (Exception ignored) {}
        }
    }
}
