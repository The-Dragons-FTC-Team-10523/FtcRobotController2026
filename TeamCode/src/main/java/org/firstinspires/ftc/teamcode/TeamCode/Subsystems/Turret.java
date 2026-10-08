package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Turret {
    public DcMotor flyPollen, flyNectar;
    public Servo pollenServo1, pollenServo2, nectarServo1, nectarServo2;
    public Turret(HardwareMap hardwareMap) {
        flyPollen = hardwareMap.get(DcMotor.class, "flyPollen");
        flyNectar = hardwareMap.get(DcMotor.class, "flyNectar");
        flyPollen.setDirection(DcMotorSimple.Direction.FORWARD);
        flyNectar.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}
