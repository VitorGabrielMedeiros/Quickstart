
package org.firstinspires.ftc.teamcode.TeleOp;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode {

    private final Robot robot;
    public Follower follower;
    public final PoseFactory start = PoseFactory.radians();
    private final Pose startPose = start.of(133.34375,7.5,Math.toRadians(90));
    private Servo servo1;
    private Servo servo2;

//    private final Servo servo1 = Servo("servo1");
//    private final Servo servo2 = Servo("servo2");

    private double xRobot;
    private double yRobot;
    private double heading;
    public double currentDistance;
    public double currentPose;

    private double xHive;
    private double yHive;
    public double lastHeading;
    public double voltas;
    public double delta;
    public double anguloTotal;
    public double flywheel;
    public double turretAngle;
    CommandGamepad gp2 = new CommandGamepad(gamepad2);
    Command delay = waitMs(300);

    public static double posMaior = 2020;
    public static double posMenor = 500;
    public double autoshoot(double distancia){
         flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
         return -flywheel;
    }


    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;
    }


    @Override
    public void periodic() {


        // Atualiza a estimativa de pose do Pedro
        follower.update();
        DrivePowers powers = ManualDrive.fieldCentric(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        currentDistance = follower.pose().distance(new Pose(xHive, yHive));


        // Le a posicao e a orientacao do robo
        xRobot = follower.pose().x();
        yRobot = follower.pose().y();
        heading = Math.toDegrees(follower.pose().heading());

        //turretAngle = robot.getTurret().getTurretAngleDegrees();


        delta = heading - lastHeading;

        if (delta < -180) {
            voltas = voltas + 1;
        }
        if (delta > 180) {
            voltas = voltas - 1;
        }

        anguloTotal = heading + (voltas * 360);

        // Escolhe o alvo
        if (yRobot > 70.5) {
            xHive = 84;
            yHive = 88;
        } else {
            xHive = 84;
            yHive = 55;
        }

        robot.getShooter().setSetPoint(autoshoot(currentDistance));
        robot.getLock().lock().schedule();

        robot.getTurret().moveServos(xRobot, yRobot, xHive, yHive, anguloTotal);

        servo1.setPosition(1-(robot.getTurret().getTurretServo()));
        servo2.setPosition(1-(robot.getTurret().getTurretServo()));

        Telemetry.log("Status", "Running");
        Telemetry.log("Robot X", xRobot);
        Telemetry.log("Robot Y", yRobot);
        Telemetry.log("servoPos", robot.getTurret().getTurretServo());
        Telemetry.log("theta" , robot.getTurret().getFieldAngleDegrees());
        Telemetry.log("Heading",anguloTotal);
        Telemetry.log("Turret Angle", robot.getTurret().getTurretAngleDegrees());
        Telemetry.log("distancia", currentDistance);
        Telemetry.log("flywheel", flywheel);
        Telemetry.log("Shooter (ticks/s)", robot.getShooter().getVel());

        Telemetry.update();

        lastHeading = heading;
    }



    @Override
    public void start() {

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        //currentPose = follower.pose();
        follower = Constants.create(hardwareMap);

        follower.setPose(startPose);
        follower.update();

        //robot.drivetrain.startDrive(gamepad1, follower);

        follower.update();


        //intake + transfer
        gp2.leftBumper().whileTrue(robot.getIntake().in().with(robot.getTransfer().in()));
        gp2.leftBumper().whileFalse(robot.getIntake().stop().with(robot.getTransfer().stop()));


        //inverse
        gp2.y().whileTrue(robot.getIntake().out().with(robot.getTransfer().out()));
        gp2.y().whileFalse(robot.getIntake().stop().with(robot.getTransfer().stop()));

        ((ServoImplEx) servo1).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );
        ((ServoImplEx) servo2).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );

        Command Shoot = sequential(
                robot.getLock().unlock(),
                robot.getIntake().in().with(robot.getTransfer().in()),
                delay,
                robot.getIntake().stop().with(robot.getTransfer().stop())
        );

        Command noShoot = parallel(
                robot.getLock().lock(),
                robot.getIntake().stop().with(robot.getTransfer().stop())
        );


        gp2.rightBumper().whileTrue(Shoot);
        gp2.rightBumper().whileFalse(noShoot);

//        if (gamepad1.a){
//
//        }
        //gp2.rightBumper().whileFalse(robot.getIntake().stop().with(robot.getTransfer().stop().with(robot.getLock().lock())));










    }
}
