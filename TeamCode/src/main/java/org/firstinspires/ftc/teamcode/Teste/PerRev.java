
package org.firstinspires.ftc.teamcode.Teste;

import static dev.nextftc.units.Units.Degrees;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Robot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;

@Config
@NextTeleop
public class PerRev extends NextOpMode {

    // 1 grau por contagem para facilitar a leitura
    private final NextMotor shooter = new NextMotor("shooter", Degrees.of(1.0));
    private double initialPosition = 0;
    private boolean previousA = false;

    public PerRev(Robot robot) {
        super(robot);
    }

    @Override
    public void start() {
        initialPosition = shooter.getEncoderPosition().getMagnitude();
    }

    @Override
    public void periodic() {


        double currentPosition = shooter.getEncoderPosition().getMagnitude();

        double ticks = currentPosition - initialPosition;

        if (gamepad1.a && !previousA) {
            initialPosition = currentPosition;
        }

        previousA = gamepad1.a;
                Telemetry.log("Posicao atual", currentPosition);
        Telemetry.log("Ticks desde zero", ticks);
        Telemetry.log("Ticks absolutos", Math.abs(ticks));
        Telemetry.update();
    }
}
