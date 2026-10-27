package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Turret {
    public DcMotorEx flyPollen, flyNectar;
    public CRServo pollenServo1, pollenServo2, nectarServo1, nectarServo2;
    public servoGroup pollenServos, nectarServos;
    public Turret(HardwareMap hardwareMap) {
        flyPollen = hardwareMap.get(DcMotorEx.class, "flyPollen");
        flyNectar = hardwareMap.get(DcMotorEx.class, "flyNectar");
        flyPollen.setDirection(DcMotorSimple.Direction.FORWARD);
        flyNectar.setDirection(DcMotorSimple.Direction.FORWARD);
        pollenServo1 = hardwareMap.get(CRServo.class, "pollenServo1");
        pollenServo2 = hardwareMap.get(CRServo.class, "pollenServo2");
        nectarServo1 = hardwareMap.get(CRServo.class, "nectarServo1");
        nectarServo2 = hardwareMap.get(CRServo.class, "nectarServo2");
        pollenServos = new servoGroup(pollenServo1, pollenServo2);
        nectarServos = new servoGroup(nectarServo1, nectarServo2); //WE'RE GONNA HAVE EXTERNAL ENCODERS ON THESE
    }
}
