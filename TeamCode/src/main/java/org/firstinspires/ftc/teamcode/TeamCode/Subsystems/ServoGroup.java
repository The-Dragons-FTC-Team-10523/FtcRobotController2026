package org.firstinspires.ftc.teamcode.TeamCode.Subsystems;
import com.qualcomm.robotcore.hardware.CRServo;

public class ServoGroup {
    private CRServo leader, follower;
    public ServoGroup(CRServo leader, CRServo follower) {
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
// encoder external? Might change to internal
}
