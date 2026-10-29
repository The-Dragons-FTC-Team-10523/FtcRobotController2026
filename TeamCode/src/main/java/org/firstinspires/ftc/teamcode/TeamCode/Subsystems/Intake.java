package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    public DcMotorEx intakeMotor, transferMotor;
    public Servo pollenWall, nectarWall;
    public CRServo flowerServo1, flowerServo2;
    public ServoGroup flowerServo;
    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotorEx.class, "transferMotor");
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        transferMotor.setDirection(DcMotorEx.Direction.FORWARD);
        pollenWall = hardwareMap.get(Servo.class, "pollenWall");
        nectarWall = hardwareMap.get(Servo.class, "nectarWall");
        flowerServo1 = hardwareMap.get(CRServo.class, "flowerServo1");
        flowerServo2 = hardwareMap.get(CRServo.class, "flowerServo2");
        flowerServo = new ServoGroup(flowerServo1, flowerServo2);
    }
}