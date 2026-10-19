package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class servoGroup {
    private CRServo leader, follower;
    public servoGroup(CRServo leader, CRServo follower) {
        this.leader = leader;
        this.follower = follower;
    }
    public void setPower(double power) {
        leader.setPower(power);
        follower.setPower(power);
    }
    public void setDirection(CRServo.Direction leaderDirection, CRServo.Direction followerDirection) {
        leader.setDirection(leaderDirection);
        follower.setDirection(followerDirection);
    }

}
