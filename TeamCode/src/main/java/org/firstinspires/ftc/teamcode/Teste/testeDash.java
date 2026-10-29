package org.firstinspires.ftc.teamcode.Teste;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
@TeleOp(name = "servo", group = "LinearOpMode")
public class testeDash extends LinearOpMode {

    private DcMotor motor;
    public static volatile double low = 0.0;
    public static double mid = 0.5;
    public static double high = 1.0;


    @Override
    public void runOpMode() {

        motor = hardwareMap.get(DcMotor.class, "fr");



        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                motor.setPower(high);
            } else if (gamepad1.b) {
                motor.setPower(mid);

            } else {
                motor.setPower(low);
            }

            telemetry.addData("low", low);
            telemetry.addData("mid", mid);
            telemetry.addData("high", high);
            telemetry.update();


        }
    }
}


