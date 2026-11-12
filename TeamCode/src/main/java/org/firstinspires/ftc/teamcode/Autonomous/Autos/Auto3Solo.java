package org.firstinspires.ftc.teamcode.Autonomous.Autos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.bate3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goFlower3S1;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goIntakeM3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goShoot3S1;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goShoot3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goShoot3S3;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.goShoot3S4;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.intake13S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.intakeFlower3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.intakeM3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.pathPark3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.vaiFlower3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.volta3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.startPose3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.voltaIntakeg3S;
import static org.firstinspires.ftc.teamcode.TeleOp.TeleOp.timeInverseIntake;
import static org.firstinspires.ftc.teamcode.TeleOp.TeleOp.timeServo;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous (name="Auto 3 Solo")
public class Auto3Solo extends NextOpMode {

    private final NextColorDistanceSensor sensorC = new NextColorDistanceSensor("sensorC");

    private Robot robot;
    private Follower follower;

    public Auto3Solo(Robot robot) {
        super(robot);
        this.robot = robot;
    }

    public Pose poseSotm;
    private Servo servo1;
    private Servo servo2;
    public static double offSetShooter = 40;

    private double xRobot;
    private double yRobot;
    private double heading;
    public double currentDistance;
    private double xHive = 0;
    private double yHive = 0;
    public double lastHeading;
    public double voltas;
    public double delta;
    public double anguloTotal;
    public double flywheel;
    Command delay = waitMs(500);
    Command delay1 = waitMs(2400);

    Command delayServo = waitMs(2500);

    public static double posMaior = 2020;
    public static double posMenor = 500;

    public double autoshoot(double distancia) {
        flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
        return (-flywheel);
    }


    public double sotm(double distanciasotm) {
        return (0.000534998 * Math.pow(distanciasotm, 2) - 0.0419473 * distanciasotm + 1.28217);
    }

    public static double clamp(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }
//
//    private Command shoot() {
//        return sequential(
//                (robot.getLock().lock()
//                        .then(delay)
//                        .raceWith(robot.getIntake().in().with(robot.getTransfer().in().with(robot.getFlower().ServoShoot().with(robot.getFlower().ServoShoot1()))))
//                        .then(delay)
//                        .raceWith(robot.getLock().unlock())));
//    }
//
//    private Command flower() {
//        return sequential(
//                (robot.getFlower().flower().with(robot.getFlower().flower1())
//                        .then(delay)
//                        .then(robot.getLock().lock().with (robot.getIntake().in().with(robot.getTransfer().in()))))
//                        .then(delayServo).then(robot.getLock().unlock().with(robot.getIntake().stop()).with(robot.getTransfer().stop()).then(delay)));
//    }
//
//    private Command intakeGarden(){
//        return sequential( follow(follower, intake13S())
//                .then(robot.getIntake().in().with(robot.getTransfer().in()
//                        .then(follow(follower, volta3S()))
//                        .then(follow(follower, bate3S()))
//                        .then(delay.with(robot.getIntake().stop()).with(robot.getTransfer().stop()))
//        )));
//    }
//    private Command shootFlower(){
//        return sequential (follow(follower, goShoot3S1())
//                .then(shoot())
//                .then(flower().then(follow(follower, goFlower3S1()))))
//                .then(follow(follower, goShoot3S2()))
//                .then(robot.getFlower().levantado()).with(robot.getFlower().levantado1());
//    }

//    private Command intakeMeio(){
//        return follow(follower, goIntakeM3S())
//                .then(robot.getIntake().in().with(robot.getTransfer().in()))
//                .then(follow(follower, intakeM3S()))
//                .then(follow(follower, goShoot3S3()));
//    }

    private Command servo1(double pos){
        return instant(() -> servo1.setPosition(clamp(1-(pos), 0 ,1)));
    }
    private Command servo2(double pos2){
        return instant(() -> servo2.setPosition(clamp(1-(pos2), 0 ,1)));
    }
    private Command auto() {
        return sequential(
                servo1(0).with(servo2(0)),
                delayServo,
                robot.getLock().lock(),
                delayServo
                        .raceWith(
                                delayServo,
                                robot.getIntake().in().with(
                                        robot.getTransfer().in(),
                                        robot.getFlower().ServoShoot(),
                                        robot.getFlower().ServoShoot1())),
                delayServo,
                servo1(0.3).with(servo2(0.3)),
                robot.getLock().unlock(),
                delay
                        .raceWith(
                                robot.getIntake().stop().with(
                                        robot.getTransfer().stop(),
                                        robot.getFlower().levantado(),
                                        robot.getFlower().levantado1())),
                delay,

                        robot.getIntake().in().with(robot.getTransfer().in()
                        .then(follow(follower, intake13S())
                        .then(follow(follower, volta3S()))
                        .then(follow(follower, bate3S())))),
                        delay,
                        robot.getIntake().stop().with(robot.getTransfer().stop())


                        .then(follow(follower, goShoot3S1())),
                delayServo,
                robot.getLock().lock(),
                delayServo
                        .raceWith(
                                delayServo,
                                robot.getIntake().in().with(
                                        robot.getTransfer().in(),
                                        robot.getLock().lock(),
                                        robot.getFlower().ServoShoot(),
                                        robot.getFlower().ServoShoot1())),
                delayServo,
                robot.getLock().unlock(),
                delay
                        .raceWith(
                                robot.getIntake().stop().with(
                                        robot.getTransfer().stop(),
                                        robot.getFlower().levantado(),
                                        robot.getFlower().levantado1())),
                delay
                        .then(follow(follower, goFlower3S1()).with(robot.getFlower().flower().with(robot.getFlower().flower1()).with(robot.getIntake().in()).with(robot.getTransfer().in()).with(robot.getLock().lock())))
                        .then(follow(follower, goShoot3S2()))

                        .then(follow(follower, goIntakeM3S()))
                        .then(follow(follower, intakeM3S()))
                        .then(follow(follower, goShoot3S3()))

                        .then(follow(follower, vaiFlower3S2()))
                        .then(follow(follower, intakeFlower3S2()))
                        .then(follow(follower, goShoot3S4()))

                        .then(follow(follower, pathPark3S()))

        );
    }


    @Override
    public void disabledPeriodic() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose3S);
        follower.update();
    }

    @Override
    public void start() {

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        schedule(
                auto()
        );
    }

    @Override
    public void periodic() {
        follower.update();
        Scheduler.execute();

        if (yRobot > 70.75) {
            xHive = 81.5;
            yHive = 86.5;
        } else {
            xHive = 84;
            yHive = 55;
        }

        robot.getShooter().setSetPoint(autoshoot(follower.pose().distance(new Pose(xHive, yHive))));

//        servo1.setPosition(clamp(1-(0.5), 0 ,1));
//        servo2.setPosition(clamp(1-(0.5), 0, 1));

        xRobot = follower.pose().x();
        yRobot = follower.pose().y();
        heading = Math.toDegrees(follower.pose().heading());

        double turretAngle = robot.getTurret().getTurretAngleDegrees();

        delta = heading - lastHeading;

        if (delta < -180) {
            voltas = voltas + 1;
        }
        if (delta > 180) {
            voltas = voltas - 1;
        }

        anguloTotal = heading + (voltas * 360);

        sensorC.update();

//        double flightTime = sotm(currentDistance);


        // Escolhe o alvo
        if (yRobot > 70.75) {
            xHive = 84;
            yHive = 86.5;
        } else {
            xHive = 81.5;
            yHive = 55;
        }

        currentDistance = clamp(follower.pose().distance(new Pose(xHive, yHive)),23,80);

//        xHive -= follower.velocity().vx * flightTime;
//        yHive -= follower.velocity().vy * flightTime;

//        double predictedX = xRobot + follower.velocity().vx * flightTime;
//        double predictedY = yRobot + follower.velocity().vy * flightTime;
//        poseSotm = new Pose((predictedX), (predictedY), heading);

//        double shootingsotm = poseSotm.distance(new Pose(xHive, yHive));

//        if (follower.velocity().vx < 25 && follower.velocity().vy < 25){
//            robot.getTransfer().setVel(1);
//            robot.getIntake().setVel(-1);
//        } else {
//            robot.getTransfer().setVel(0);
//            robot.getIntake().setVel(0);
//        }

        robot.getShooter().setSetPoint(autoshoot(currentDistance));


//        servo1.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0 ,1));
//        servo2.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0, 1));

        lastHeading = heading;

        Telemetry.log("distancia", currentDistance);
        Telemetry.log("x", xRobot);
        Telemetry.log("y", yRobot);
        Telemetry.log("sensor", sensorC.getColor().getGreen());
        Telemetry.update();
    }

        //ola


    }
