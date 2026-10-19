package org.firstinspires.ftc.teamcode.TeamCode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Turret;

@TeleOp(name = "FlyWheelTest", group = "TeleOp")
public class FlyWheelTest extends LinearOpMode {
    //connected to the subsystem

    public Turret turret;

    // Bare 5000-series motor resolution is exactly 28 ticks per revolution
    static final double TICKS_PER_REV = 28.0;

    // Max theoretical speed is 6000 RPM -> 100 revs per second -> 2800 ticks/sec
    static final double MAX_TICKS_PER_SEC = 2800.0;

    @Override
    public void runOpMode() {
        // Map two flywheel motors
        turret = new Turret(hardwareMap);
        // CRITICAL FLYWHEEL SAFETY: Use FLOAT instead of BRAKE.
        turret.flyPollen.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        turret.flyNectar.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Set to closed-loop velocity control mode
        turret.flyPollen.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.flyNectar.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Dual Flywheels Configured. A = FlyWheel1, Y = FlyWheel2.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double targetVelA = gamepad1.a ? (MAX_TICKS_PER_SEC) : 0.0;
            turret.flyPollen.setVelocity(-targetVelA);
            double targetVelY = gamepad1.y ? (MAX_TICKS_PER_SEC) : 0.0;
            turret.flyNectar.setVelocity(-targetVelY);

            // Calculate current RPM readings for feedback
            double currentRpmA = (turret.flyPollen.getVelocity() / TICKS_PER_REV) * 60.0;
            double currentRpmY = (turret.flyNectar.getVelocity() / TICKS_PER_REV) * 60.0;

            telemetry.addData("Motor 1 (A Pressed: " + gamepad1.a + ")", "Target: %.0f RPM | Actual: %.0f RPM", (targetVelA / TICKS_PER_REV) * 60.0, currentRpmA);
            telemetry.addData("Motor 2 (Y Pressed: " + gamepad1.y + ")", "Target: %.0f RPM | Actual: %.0f RPM", (targetVelY / TICKS_PER_REV) * 60.0, currentRpmY);
            telemetry.update();
        }
    }
}
