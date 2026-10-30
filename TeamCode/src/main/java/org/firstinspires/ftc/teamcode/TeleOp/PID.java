
package org.firstinspires.ftc.teamcode.TeleOp;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.DegreesPerSecond;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Robot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;

@Config
@NextTeleop
public class PID extends NextOpMode {

    // Encoder
    public static double TICK_PER_REV = 27;

    // Setpoint em ticks/s
    public static double setpoint = -1000.0;

    // Ganhos PID
    public static double kp = 0.0;
    public static double ki = 0.0;
    public static double kd = 0.0;
    public static double kv = 0.0;
    public static double ks = 0.0;
    public static double ka = 0.0;

    private final NextMotor shooter = new NextMotor("shooter", Degrees.of(360.0 / TICK_PER_REV));

    private final NextMotor shooter1 = new NextMotor("shooter1", Degrees.of(360.0 / TICK_PER_REV));

    private final NextMotor intake = new NextMotor("intake");

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

        shooter.getVelocityConstants()
                .withP(kp)
                .withI(ki)
                .withD(kd)
                .withV(kv)
                .withA(ka)
                .withS(ks);

        shooter1.getVelocityConstants()
                .withP(kp)
                .withI(ki)
                .withD(kd)
                .withV(kv)
                .withA(ka)
                .withS(ks);

        // Shooter
        if (gamepad1.a) {

            double velocityAngle = ticksToDegrees(setpoint);

            shooter.setVelocitySetpoint(DegreesPerSecond.of(velocityAngle));

            shooter1.setVelocitySetpoint(DegreesPerSecond.of(velocityAngle));

            shooter.update();
            shooter1.update();

        } else {

            shooter.setThrottle(0);
            shooter1.setThrottle(0);
        }

        // Intake
        if (gamepad1.right_bumper) {
            intake.setThrottle(1.0);
        } else {
            intake.setThrottle(0.0);
        }

        // Telemetria
        Telemetry.log("Setpoint (ticks/s)", setpoint);

        Telemetry.log("Shooter (ticks/s)", getVel(shooter));
        Telemetry.log("Shooter1 (ticks/s)", getVel(shooter1));

        Telemetry.log("Kp", kp);
        Telemetry.log("Ki", ki);
        Telemetry.log("Kd", kd);

        Telemetry.update();
    }

    @Override
    public void start() {
    }
}
