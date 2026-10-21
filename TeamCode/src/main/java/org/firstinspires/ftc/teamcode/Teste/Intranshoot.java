package org.firstinspires.ftc.teamcode.Teste;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "intake", group = "LinearOpMode")
public class Intranshoot extends LinearOpMode {

    private DcMotorEx intake;
    private DcMotorEx transfer;
    private DcMotorEx shooter;
    private DcMotorEx shooter1;

    private double potencia = 1;

    // Velocidade-alvo em ticks por segundo.
    // Ajuste conforme a velocidade desejada para sua flywheel.
    private double alvoVelocidade = 1300;

    // Ganhos PIDF iniciais para calibração.
    // Estes valores não são universais para todos os motores.
    private final PIDFCoefficients pidf = new PIDFCoefficients(
            18,
            0.0,
            0.0,
            15
    );

    @Override
    public void runOpMode() {

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter1.setDirection(DcMotor.Direction.FORWARD);

        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER, pidf
        );
        shooter1.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER, pidf
        );

        waitForStart();

        while (opModeIsActive()) {

            double vel = shooter.getVelocity();
            double vel1 = shooter1.getVelocity();

            // Intake e transfer
            if (gamepad2.right_bumper) {
                intake.setPower(1);
                transfer.setPower(1);
            } else if (gamepad2.left_bumper) {
                intake.setPower(-1);
                transfer.setPower(-1);
            } else {
                intake.setPower(0);
                transfer.setPower(0);
            }

            // Flywheels com controle PIDF
            if (gamepad2.a) {
                shooter.setVelocity(alvoVelocidade);
                shooter1.setVelocity(alvoVelocidade);
            } else {
                shooter.setPower(0);
                shooter1.setPower(0);
            }

            telemetry.addData("Alvo (ticks/s)", alvoVelocidade);
            telemetry.addData("Shooter (ticks/s)", vel);
            telemetry.addData("Shooter 1 (ticks/s)", vel1);
            telemetry.addData("Erro shooter", alvoVelocidade - vel);
            telemetry.addData("Erro shooter 1", alvoVelocidade - vel1);
            telemetry.addData("Potencia inicial", potencia);
            telemetry.update();
        }

        shooter.setPower(0);
        shooter1.setPower(0);
        intake.setPower(0);
        transfer.setPower(0);
    }
}
