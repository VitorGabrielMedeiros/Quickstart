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

    private Servo flower2;
    private Servo flower1;
    private Servo lock;
    public static double Posshoot = 0.10;
    public static double posHigh = 0.5;
    public static double posflower = 0.0;
    public static double defense = 0.18;

    public static double posMaior = 2500;
    public static double posMenor = 500;
    public static double lock1 = 0.3;
    public static double unlock = 0.15;

    @Override
    public void runOpMode() {

        flower1 = hardwareMap.get(Servo.class, "flower1");
        flower2 = hardwareMap.get(Servo.class, "flower2");
        lock = hardwareMap.get(Servo.class, "lock");


//        ((ServoImplEx) flower1).setPwmRange(
//                new PwmControl.PwmRange(posMenor, posMaior)
//        );
//        ((ServoImplEx) flower2).setPwmRange(
//                new PwmControl.PwmRange(posMenor, posMaior)
//        );

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                lock.setPosition(lock1);
            } else {
                lock.setPosition(unlock);
            }
        }
    }
}


