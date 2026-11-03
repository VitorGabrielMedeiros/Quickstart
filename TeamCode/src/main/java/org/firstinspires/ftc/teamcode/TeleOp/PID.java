/*
package org.firstinspires.ftc.teamcode.TeleOp;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.DegreesPerSecond;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;

@Config
@NextTeleop
public class PID extends NextOpMode {


    // Encoder
    public static double TICK_PER_REV = 34;

    // Setpoint em ticks/s
    public static double setpoint = 0;

    // Ganhos PID
    public static double kp = 0,00003;
    public static double ki = 0.0;
    public static double kd = 0.000000;
    public static double kv = 0,000038;
    public static double ks = 0,00055;
    public static double ka = 0.000;
    double xHive = 84;
    double yHive = 70.94;
    public final PoseFactory start = PoseFactory.radians();
    private final Pose startPose = start.of(133.34375,7.5,Math.toRadians(90));

    double currentDistance;
    public Follower follower;

    private final NextMotor shooter = new NextMotor("shooter", Degrees.of(360.0 / TICK_PER_REV));

    private final NextMotor shooter1 = new NextMotor("shooter1", Degrees.of(360.0 / TICK_PER_REV));

    private final NextMotor intake = new NextMotor("intake");

    private  final NextMotor transfer = new NextMotor("transfer");


    public PID(Robot robot) {
        super(robot);
    }

    public double ticksToDegrees(double ticks) {
        return ticks * (360.0 / TICK_PER_REV);
    }

    public double getVel(NextMotor motor) {

        double velocityDegrees = motor.getEncoderVelocity().getMagnitude();
        double degreesPerTick = 360.0 / TICK_PER_REV;
        return velocityDegrees / degreesPerTick;
    }

    @Override
    public void periodic() {
        follower.update();
        currentDistance = follower.pose().distance(new Pose(xHive, yHive));

        shooter.getVelocityConstants()
                .withP(kp)
                .withI(ki)
                .withD(kd)
                .withV(kv)
                .withA(ka)
                .withS(ks);

        double velocityAngle = ticksToDegrees(setpoint);

        shooter.setVelocitySetpoint(DegreesPerSecond.of(velocityAngle));

        shooter1.follow(shooter);


        // Shooter

        // Intake
        if (gamepad1.right_bumper) {
            intake.setThrottle(1.0);
            transfer.setThrottle(-1.0);
        } else {
            intake.setThrottle(0.0);
            transfer.setThrottle(0.0);
        }



        Telemetry.log("Setpoint (ticks/s)", setpoint);
        Telemetry.log("Shooter (ticks/s)", getVel(shooter));
        Telemetry.log("Shooter1 (ticks/s)", getVel(shooter1));
        Telemetry.log("currentDistance", currentDistance);
        Telemetry.update();
    }

    @Override
    public void start() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

    }
}
*/