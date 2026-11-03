package org.firstinspires.ftc.teamcode.Teste;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
@TeleOp(name = "servo", group = "LinearOpMode")
public class ServoTeste extends LinearOpMode {

    private Servo servo1;
    private Servo servo2;
    private Servo lock;
    public static double posLock = 1.0;
    public static double posMid = 0.5;
    public static double posAberto = 0.0;
    public static double posMaior = 2500;
    public static double posMenor = 500;

    @Override
    public void runOpMode() {

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        lock = hardwareMap.get(Servo.class, "lock");


        ((ServoImplEx) servo1).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );
        ((ServoImplEx) servo2).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                lock.setPosition(posLock);
            } else if (gamepad1.b) {
                lock.setPosition(posMid);
            } else {
                lock.setPosition(posAberto);
            }
        }
    }
}


