
package org.firstinspires.ftc.teamcode.Teste;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name = "Shooter Bang Bang" , group = "LinearOpMode")
public class shooterbangbang extends LinearOpMode {

    private DcMotorEx intake;
    private DcMotorEx transfer;
    private DcMotorEx shooter;
    private DcMotorEx shooter1;
    // MECANICA: ajuste para o seu robo
    public static double FLYWHEEL_RPM = 2960;

    // RPM do motor / RPM do flywheel
    // 1.0 = acionamento direto, 1:1
    static final double MOTOR_TO_FLYWHEEL = 1.0;

    // REV HD Hex sem caixa: 28 contagens por volta
    static final double TICKS_PER_MOTOR_REV = 28.0;

    // Controle
    public static double HYSTERESIS = 0.03;
    public static double HIGH_POWER = 0.69;
    public static double HOLD_POWER = 0.66;

    // Limite inicial de velocidade do motor
    // Verifique os limites mecanicos ante"s de usar.
    public static final double MAX_RPM = 6000;

    @Override
    public void runOpMode() {

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter.setDirection(DcMotor.Direction.REVERSE);
        shooter1.setDirection(DcMotor.Direction.REVERSE);

        shooter.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.FLOAT
        );
        shooter1.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.FLOAT
        );

        shooter.setMode(
                DcMotor.RunMode.RUN_WITHOUT_ENCODER
        );
        shooter1.setMode(
                DcMotor.RunMode.RUN_WITHOUT_ENCODER
        );

        double targetTPS =
                FLYWHEEL_RPM
                        * MOTOR_TO_FLYWHEEL
                        * TICKS_PER_MOTOR_REV / 60.0;

        double lowerTPS =  targetTPS * (1.0 - HYSTERESIS);
        double upperTPS = targetTPS * (1.0 + HYSTERESIS);
        double maxTPS = MAX_RPM
                * TICKS_PER_MOTOR_REV / 60.0;

        double power = 0.0;
        boolean enabled = false;
        boolean lastA = false;

        telemetry.addLine("Shooter Bang Bang");
        telemetry.addData("Target TPS", targetTPS);
        telemetry.addData("Target flywheel RPM", FLYWHEEL_RPM);
        telemetry.addLine("A = liga/desliga");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            boolean a = gamepad1.a;

            if (a && !lastA) {
                enabled = !enabled;
            }
            lastA = a;

            double motorTPS = Math.abs(shooter.getVelocity());

            if (!enabled) {
                power = 0.0;

            } else if (motorTPS >= maxTPS) {
                // Protecao adicional contra sobrevelocidade
                power = 0.0;

            } else if (motorTPS < lowerTPS) {
                // Recuperar velocidade
                power = HIGH_POWER;

            } else if (motorTPS > upperTPS) {
                // Reduzir potencia, mantendo sustentacao
                power = HOLD_POWER;
            }

            shooter.setPower(power);
            shooter1.setPower(power);


            double estimatedMotorRPM =
                    motorTPS * 60.0 / TICKS_PER_MOTOR_REV;

            double estimatedFlywheelRPM =
                    estimatedMotorRPM / MOTOR_TO_FLYWHEEL;

            telemetry.addData("Enabled", enabled);
            telemetry.addData("Motor TPS", motorTPS);
            telemetry.addData("Target TPS", targetTPS);
            telemetry.addData("Flywheel RPM estimado",
                    estimatedFlywheelRPM);
            telemetry.addData("Potencia", power);
            telemetry.addData("Limite TPS", maxTPS);
            telemetry.update();
            if (gamepad1.right_bumper) {
                intake.setPower(1);
                transfer.setPower(-1);

            } else if (gamepad2.left_bumper) {
                intake.setPower(1);
                transfer.setPower(-1);
            } else {
                intake.setPower(0);
                transfer.setPower(0);
            }
        }

        shooter.setPower(0.0);
    }
}
