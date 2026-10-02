package org.firstinspires.ftc.teamcode.TeamCode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.Cameras;
import org.firstinspires.ftc.teamcode.TeamCode.Subsystems.RobotClass;

@TeleOp(name = "MecanumDrive")
public class MecanumDrive extends LinearOpMode {
    RobotClass robotClass;
//    Cameras Cameras;
    double[] wheelSpeeds = {0,0,0,0};

    @Override
    public void runOpMode() throws InterruptedException {

        robotClass = new RobotClass(hardwareMap);
//        Cameras = new Cameras(hardwareMap);

        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.start){
                robotClass.resetIMU();
            }
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;

            double rx = gamepad1.right_trigger - gamepad1.left_trigger;

            double heading = robotClass.getHeading(AngleUnit.RADIANS);

            double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
            double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

            telemetry.addData("rotY", rotY);
            telemetry.addData("rotX", rotX);
            telemetry.addData("heading", robotClass.getHeading(AngleUnit.DEGREES));
            telemetry.update();

            wheelSpeeds[0] = (rotY + rotX + rx); //FRONT_LEFT
            wheelSpeeds[1] = (rotY - rotX + rx); //BACK_LEFT
            wheelSpeeds[2] = (rotY - rotX - rx); //FRONT_RIGHT
            wheelSpeeds[3] = (rotY + rotX - rx); //BACK_RIGHT

            double largest = 1;
            for(double speed : wheelSpeeds){
                largest = speed > 1 ? speed : 1;
            }
            telemetry.addData("denominator", largest);



            wheelSpeeds[0] /= 1.0126;
            wheelSpeeds[1] /= 1;
            wheelSpeeds[2] /= 1.0457;
            wheelSpeeds[3] /= 1.0116;

            robotClass.frontLeft.setPower(wheelSpeeds[0] / largest);
            robotClass.backLeft.setPower(wheelSpeeds[1] / largest);
            robotClass.frontRight.setPower(wheelSpeeds[2] / largest);
            robotClass.backRight.setPower(wheelSpeeds[3] / largest);
        }
    }
}