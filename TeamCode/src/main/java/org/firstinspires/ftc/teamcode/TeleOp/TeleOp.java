package org.firstinspires.ftc.teamcode.TeleOp;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.ftc.ParallelOTOSEncoder;

@Config

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode {

    private  final NextColorDistanceSensor sensorC = new NextColorDistanceSensor("sensorC");

    private final Robot robot;
    public Follower follower;
    public final PoseFactory start = PoseFactory.radians();
    private final Pose startPose = start.of(133.34375,7.5,Math.toRadians(90));
    public Pose poseResetOpostFLower = new Pose(58.6,133.9,Math.toRadians(270));
    public Pose poseReset = new Pose (106, 7.5, Math.toRadians(90));
    public Pose poseSotm;
    private Servo servo1;
    private Servo servo2;
    private boolean lastDpadUp = false;
    private boolean lastDpadDown = false;
    public static double offSetShooter = 40;

//    private final Servo servo1 = Servo("servo1");
//    private final Servo servo2 = Servo("servo2");

    private double xRobot;
    private double yRobot;
    private double heading;
    public double currentDistance;
    public double currentPose;
    private double xHive = 0;
    private double yHive = 0;
    public double lastHeading;
    public double voltas;
    public double delta;
    public double anguloTotal;
    public double flywheel;
    public static double minMov = 0.0;
    public static double maxMov = 0.0;
    public double turretAngle;
    CommandGamepad gp2 = new CommandGamepad(gamepad2);
    Command delay = waitMs(300);
    Command delayServo = waitMs(100);
    public static double movVelMin = -0.2;
    public static double movVelMax = 0.2;
    public static double quantidadeG = 0.9;
    public static double timeServo = 100;
    public static double timeInverseIntake = 50;


    public static double posMaior = 2020;
    public static double posMenor = 500;
    public double autoshoot(double distancia){
        flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
        return (-flywheel-offSetShooter);
    }

    public double sotm(double distanciasotm){
        return (0.000534998 * Math.pow(distanciasotm, 2)-0.0419473 * distanciasotm+1.28217);
    }


    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;
    }

    public static double clamp (double valor, double min, double max){
        return Math.max(min, Math.min(max, valor));
    }


    @Override
    public void periodic() {

        // Atualiza a estimativa de pose do Pedro
        follower.update();
        DrivePowers powers = ManualDrive.fieldCentric(
                clamp(gamepad1.left_stick_y, minMov, maxMov),
                clamp(gamepad1.left_stick_x, minMov, maxMov),
                clamp(-gamepad1.right_stick_x, minMov, maxMov),
                follower.pose().heading()
        );
        follower.manual(powers);

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

        sensorC.update();

        if(gamepad2.left_bumper){
            if (sensorC.getColor().getGreen() > 0.9) {
                robot.getTransfer().setPower(0);
            }else{
                robot.getTransfer().setPower(1);
            }
        }

        if(gamepad1.y || gamepad2.y){
            follower.setPose(poseReset);
        }
        if (gamepad1.x || gamepad2.x){
            follower.setPose(poseResetOpostFLower);
        }
        if (gamepad1.right_bumper){
            minMov = movVelMin;
            maxMov = movVelMax;
        } else {
            minMov = -1;
            maxMov = 1;
        }

        double flightTime = sotm(currentDistance);


        // Escolhe o alvo
        if (yRobot > 70.75) {
            xHive = 84;
            yHive = 86.5;
        } else {
            xHive = 84;
            yHive = 55;
        }

        currentDistance = clamp(follower.pose().distance(new Pose(xHive, yHive)),23,80);

        xHive -= follower.velocity().vx * flightTime;
        yHive -= follower.velocity().vy * flightTime;

        double predictedX = xRobot + follower.velocity().vx * flightTime;
        double predictedY = yRobot + follower.velocity().vy * flightTime;
        poseSotm = new Pose((predictedX), (predictedY), heading);

        double shootingsotm = poseSotm.distance(new Pose(xHive, yHive));

        // Aumentar offset
        if (gamepad2.dpad_up && !lastDpadUp) {
            offSetShooter += 10;
        }

        // Diminuir offset
        if (gamepad1.dpad_down && !lastDpadDown) {
            offSetShooter -= 10;
        }

        if (follower.velocity().vx < 25 && follower.velocity().vy < 25){
            robot.getTransfer().setVel(1);
            robot.getIntake().setVel(-1);
        } else {
            robot.getTransfer().setVel(0);
            robot.getIntake().setVel(0);
        }

        // Atualiza o estado dos botões
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;

        robot.getShooter().setSetPoint(autoshoot(shootingsotm));

        robot.getTurret().moveServos(xRobot, yRobot, xHive, yHive, anguloTotal);

        servo1.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0 ,1));
        servo2.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0, 1));




        lastHeading = heading;

        Telemetry.log("distancia", currentDistance);
        Telemetry.log("x", xRobot);
        Telemetry.log("y", yRobot);
        Telemetry.log("heading", heading);
        Telemetry.log("sensor", sensorC.getColor().getGreen());
        Telemetry.update();
    }

    @Override
    public void start() {
        robot.getLock().unlock().schedule();

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        //currentPose = follower.pose();
        follower = Constants.create(hardwareMap);

        follower.setPose(startPose);
        follower.update();

        //robot.drivetrain.startDrive(gamepad1, follower);

        follower.update();


        //intake + transfer
        gp2.leftBumper().whileTrue(robot.getIntake().in().with(robot.getLock().unlock()));
        gp2.leftBumper().toggleOnFalse(robot.getIntake().stop().with(robot.getTransfer().stop()));

        //inverse
        gp2.b().toggleOnTrue(robot.getIntake().out().with(robot.getTransfer().out()
                .then(waitMs(timeInverseIntake))
                .then(robot.getIntake().in()).with(robot.getTransfer().in())
                .then(waitMs(timeInverseIntake))
                .then(robot.getIntake().stop()).with(robot.getTransfer().stop())));
        gp2.b().toggleOnFalse(robot.getIntake().stop().with(robot.getTransfer().stop()));



        ((ServoImplEx) servo1).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );
        ((ServoImplEx) servo2).setPwmRange(
                new PwmControl.PwmRange(posMenor, posMaior)
        );

        Command Shoot = sequential(
                robot.getLock().lock(),
                delayServo,
                robot.getFlower().ServoShoot().with(robot.getFlower().ServoShoot1()),
                robot.getIntake().shoot().with(robot.getTransfer().shoot()),
                delay,
                robot.getIntake().stop().with(robot.getTransfer().stop()),
                robot.getFlower().levantado().with(robot.getFlower().levantado1())
        );

        Command noShoot = parallel(
                robot.getLock().unlock(),
                robot.getIntake().stop().with(robot.getTransfer().stop()),
                robot.getFlower().levantado().with(robot.getFlower().levantado1())
        );


        gp2.rightBumper().toggleOnTrue(robot.getLock().lock()
                .then(waitMs(timeServo))
                .then(robot.getIntake().shoot().with(robot.getTransfer().shoot().with(robot.getFlower().ServoShoot().with(robot.getFlower().ServoShoot1())))
                .then(delay)));
        gp2.rightBumper().toggleOnFalse(noShoot.with(robot.getIntake().stop()).with(robot.getTransfer().stop().with(robot.getFlower().levantado1().with(robot.getFlower().levantado()))));


        gp2.leftTrigger().isOver(0.000001).whileTrue(robot.getFlower().flower().with(robot.getFlower().flower1()));
        gp2.leftTrigger().isOver(0.000001).whileFalse(robot.getFlower().levantado().with(robot.getFlower().levantado1()));

        gp2.rightTrigger().isOver(0.000001).whileTrue(robot.getFlower().defense().with(robot.getFlower().defense1()));
        gp2.rightTrigger().isOver(0.000001).whileFalse(robot.getFlower().levantado().with(robot.getFlower().levantado1()));





//        if (gamepad1.a){
//
//        }
        //gp2.rightBumper().whileFalse(robot.getIntake().stop().with(robot.getTransfer().stop().with(robot.getLock().lock())));










    }
}


