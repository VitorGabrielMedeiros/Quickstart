package org.firstinspires.ftc.teamcode.TeleOp;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode {

    private final Robot robot;
    public Follower follower;

    public TeleOp (Robot robot){
        super(robot);
        this.robot = robot;
    }

    @Override
    public void periodic() {
        Telemetry.log("Status", "Running");
        Telemetry.update();
    }
    @Override
    public void start(){
        follower = Constants.create(hardwareMap);
        follower.update();
        robot.drivetrain.startDrive(gamepad1, follower);
    }

}
