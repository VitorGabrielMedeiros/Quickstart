package org.firstinspires.ftc.teamcode.Teste;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="MovNoField", group="Linear OpMode")
public class MovNoField extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotorEx shooter;
    private DcMotorEx shooter1;

    double currentFL = 0;
    double currentFR = 0;
    double currentBL = 0;
    double currentBR = 0;

    private DcMotorEx intake;
    private DcMotorEx transfer;

    double potencia = 1;

    @Override
    public void runOpMode() {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "fl");
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl");
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr");
        backRightDrive = hardwareMap.get(DcMotor.class, "br");

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");

        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");

        shooter1.setDirection(DcMotorEx.Direction.FORWARD);


        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);


        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad2.right_bumper){
                intake.setPower(1);
                transfer.setPower(1);
            } else if (gamepad2.left_bumper){
                intake.setPower(-1);
                transfer.setPower(-1);
            } else {
                intake.setPower(0);
                transfer.setPower(0);
            }

            if(gamepad2.a){
                shooter.setPower(potencia);
                shooter1.setPower(potencia);
            } else {
                shooter.setPower(0);
                shooter1.setPower(0);

            }



            double max = 1;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x *1.1;
            double yaw     =  gamepad1.right_stick_x;


            double frontLeftPower  = axial + lateral + yaw;
            double frontRightPower = axial - lateral - yaw;
            double backLeftPower   = axial - lateral + yaw;
            double backRightPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            double potencia = 1;

            frontLeftPower = frontLeftPower * potencia;
            frontRightPower = frontRightPower * potencia;
            backLeftPower = backLeftPower * potencia;
            backRightPower = backRightPower * potencia;

            // Send calculated power to wheels
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }}
