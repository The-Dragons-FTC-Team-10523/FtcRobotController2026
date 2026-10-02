package org.firstinspires.ftc.teamcode.TeamCode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.LimelightIntakeHelper;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.RobotClass;

/**
 * LimelightAutoIntake
 *
 * TeleOp OpMode demonstrating Limelight 3A detection and automatic intake drive.
 *
 * Controls:
 * - Driver Manual Control: Gamepad1 Left Stick (x, y) & Triggers (turn)
 * - Hold 'A' Button: Auto-track & drive towards "nectar" ball
 * - Hold 'B' Button: Auto-track & drive towards "pollen" ball
 * - Hold 'X' Button: Auto-track ANY target currently visible
 * - Press 'START': Reset IMU Heading
 */
@TeleOp(name = "Limelight Auto Intake", group = "TeleOp")
public class LimelightAutoIntake extends LinearOpMode {

    private RobotClass robotClass;
    private LimelightIntakeHelper limelightHelper;
    private final double[] wheelSpeeds = {0, 0, 0, 0};

    // Stopping distance in inches from ball center
    private static final double INTAKE_STOP_DISTANCE_INCHES = 4.0;

    @Override
    public void runOpMode() {

        robotClass = new RobotClass(hardwareMap);
        limelightHelper = new LimelightIntakeHelper(hardwareMap, "limelight");

        telemetry.setMsTransmissionInterval(11);

        // Physical camera geometry: camera height 8 in, pitch angle 15 deg, target ball height 1.5 in
        limelightHelper.setCameraMounting(8.0, 15.0, 1.5);

        telemetry.addData("Limelight Status", limelightHelper.isInitialized() ? "Initialized OK" : "FAILED to find 'limelight'");
        telemetry.addData("Instructions", "Press A for Nectar, B for Pollen, X for Any Target.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.start) {
                robotClass.resetIMU();
            }

            String activeTargetLabel = "";
            boolean isAutoTracking = false;

            if (gamepad1.a) {
                activeTargetLabel = "nectar";
                isAutoTracking = true;
            } else if (gamepad1.b) {
                activeTargetLabel = "pollen";
                isAutoTracking = true;
            } else if (gamepad1.x) {
                activeTargetLabel = ""; // Track any visible object
                isAutoTracking = true;
            }

            double rotY = 0.0;
            double rotX = 0.0;
            double rx = 0.0;

            if (isAutoTracking) {
                LimelightIntakeHelper.TargetData targetData = limelightHelper.getTargetData(activeTargetLabel, INTAKE_STOP_DISTANCE_INCHES);

                // Diagnostic Telemetry
                telemetry.addData("--- VISION DIAGNOSTICS ---", "");
                telemetry.addData("LL Result Valid", targetData.resultValid);
                telemetry.addData("Pipeline Index", targetData.pipelineIndex);
                telemetry.addData("Detectors Found", targetData.detectorCount);
                telemetry.addData("Classifiers Found", targetData.classifierCount);
                telemetry.addData("Status Text", targetData.statusText);

                if (targetData.hasTarget) {
                    rotY = targetData.drivePower;
                    rx = targetData.turnPower;

                    telemetry.addData("Locked Target", "%s (Conf: %.2f)", targetData.className, targetData.confidence);
                    telemetry.addData("Angles (deg)", "tx: %.2f, ty: %.2f, Area: %.2f", targetData.tx, targetData.ty, targetData.area);
                    telemetry.addData("Distance Est.", "%.2f inches", targetData.distanceInches);
                    telemetry.addData("Output Powers", "Drive: %.2f, Turn: %.2f", rotY, rx);
                }
            } else {
                // Manual Field-Centric Mecanum Control
                double y = -gamepad1.left_stick_y;
                double x = gamepad1.left_stick_x;
                rx = gamepad1.right_trigger - gamepad1.left_trigger;

                double heading = robotClass.getHeading(AngleUnit.RADIANS);
                rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
                rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

                telemetry.addData("Control Mode", "Manual Field-Centric Drive");
                telemetry.addData("IMU Heading (deg)", "%.1f", robotClass.getHeading(AngleUnit.DEGREES));
            }

            // Calculate Mecanum wheel speeds matching team's MecanumDrive setup
            wheelSpeeds[0] = (rotY + rotX + rx); // FRONT_LEFT
            wheelSpeeds[1] = (rotY - rotX + rx); // BACK_LEFT
            wheelSpeeds[2] = (rotY - rotX - rx); // FRONT_RIGHT
            wheelSpeeds[3] = (rotY + rotX - rx); // BACK_RIGHT

            // Normalize motor speeds
            double largest = 1.0;
            for (double speed : wheelSpeeds) {
                double absSpeed = Math.abs(speed);
                if (absSpeed > largest) {
                    largest = absSpeed;
                }
            }

            // Trim scaling constants from team's MecanumDrive
            wheelSpeeds[0] /= 1.0126;
            wheelSpeeds[1] /= 1.0;
            wheelSpeeds[2] /= 1.0457;
            wheelSpeeds[3] /= 1.0116;

            robotClass.frontLeft.setPower(wheelSpeeds[0] / largest);
            robotClass.backLeft.setPower(wheelSpeeds[1] / largest);
            robotClass.frontRight.setPower(wheelSpeeds[2] / largest);
            robotClass.backRight.setPower(wheelSpeeds[3] / largest);

            telemetry.update();
        }

        limelightHelper.stop();
    }
}
