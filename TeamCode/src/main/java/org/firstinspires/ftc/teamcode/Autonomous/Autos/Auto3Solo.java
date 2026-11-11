package org.firstinspires.ftc.teamcode.Autonomous.Autos;

import static com.pedropathing.ivy.Scheduler.schedule;
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

    private  final NextColorDistanceSensor sensorC = new NextColorDistanceSensor("sensorC");

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
    Command delay = waitMs(300);
    Command delay1 = waitMs(2400);

    Command delayServo = waitMs(100);

    public static double posMaior = 2020;
    public static double posMenor = 500;
    public double autoshoot(double distancia){
        flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
        return (-flywheel-offSetShooter);
    }



    public double sotm(double distanciasotm){
        return (0.000534998 * Math.pow(distanciasotm, 2)-0.0419473 * distanciasotm+1.28217);
    }

    public static double clamp (double valor, double min, double max){
        return Math.max(min, Math.min(max, valor));
    }

    private Command shoot() {
        return sequential(
                (robot.getLock().lock()
                        .then(waitMs(timeServo))
                        .then(robot.getIntake().shoot().with(robot.getTransfer().shoot().with(robot.getFlower().ServoShoot().with(robot.getFlower().ServoShoot1())))
                                .then(delay))));
    }

    private Command auto(){
        return sequential(
//                delay1,
//                robot.getIntake().in().with(robot.getTransfer().in()).with(robot.getLock().lock()),
//                delay,
//                robot.getIntake().out().with(robot.getTransfer().out()).with(robot.getLock().unlock()),
                follow(follower, intake13S()),

                follow(follower, volta3S()),

                follow(follower, bate3S()),

                follow(follower, goShoot3S1()),

                follow(follower, goFlower3S1()),

                follow(follower, goShoot3S2()),

                follow(follower, goIntakeM3S()),

                follow(follower, intakeM3S()),

                follow(follower, goShoot3S3()),

                follow(follower, vaiFlower3S2()),

                follow(follower, intakeFlower3S2()),

                follow(follower, goShoot3S4()),

                follow(follower, pathPark3S())

        );
    }



    @Override
    public void disabledPeriodic(){
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose3S);
        follower.update();
    }

    @Override
    public void start(){

        schedule(auto());
    }

    @Override
    public void periodic(){
        follower.update();
        Scheduler.execute();

        if (yRobot > 70.75) {
            xHive = 84;
            yHive = 86.5;
        } else {
            xHive = 81.5;
            yHive = 55;
        }

        robot.getShooter().setSetPoint(autoshoot(follower.pose().distance(new Pose(xHive, yHive))));
        robot.getTurret().moveServos(xRobot, yRobot, xHive, yHive, anguloTotal);

        servo1.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0 ,1));
        servo2.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0, 1));

//        xRobot = follower.pose().x();
//        yRobot = follower.pose().y();
//        heading = Math.toDegrees(follower.pose().heading());
//
//        //turretAngle = robot.getTurret().getTurretAngleDegrees();
//
//        delta = heading - lastHeading;
//
//        if (delta < -180) {
//            voltas = voltas + 1;
//        }
//        if (delta > 180) {
//            voltas = voltas - 1;
//        }
//
//        anguloTotal = heading + (voltas * 360);
//
//        sensorC.update();
//
//        double flightTime = sotm(currentDistance);
//
//
//        // Escolhe o alvo
//        if (yRobot > 70.75) {
//            xHive = 84;
//            yHive = 86.5;
//        } else {
//            xHive = 81.5;
//            yHive = 55;
//        }
//
//        currentDistance = clamp(follower.pose().distance(new Pose(xHive, yHive)),23,80);
//
//        xHive -= follower.velocity().vx * flightTime;
//        yHive -= follower.velocity().vy * flightTime;
//
//        double predictedX = xRobot + follower.velocity().vx * flightTime;
//        double predictedY = yRobot + follower.velocity().vy * flightTime;
//        poseSotm = new Pose((predictedX), (predictedY), heading);
//
//        double shootingsotm = poseSotm.distance(new Pose(xHive, yHive));
//
//        if (follower.velocity().vx < 25 && follower.velocity().vy < 25){
//            robot.getTransfer().setVel(1);
//            robot.getIntake().setVel(-1);
//        } else {
//            robot.getTransfer().setVel(0);
//            robot.getIntake().setVel(0);
//        }
//
//        robot.getShooter().setSetPoint(autoshoot(shootingsotm));
//
//        robot.getTurret().moveServos(xRobot, yRobot, xHive, yHive, anguloTotal);
//
//        servo1.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0 ,1));
//        servo2.setPosition(clamp(1-(robot.getTurret().getTurretServo()), 0, 1));
//
//        lastHeading = heading;
//
//        Telemetry.log("distancia", currentDistance);
//        Telemetry.log("x", xRobot);
//        Telemetry.log("y", yRobot);
//        Telemetry.log("sensor", sensorC.getColor().getGreen());
//        Telemetry.update();
    }

    public Command intakeGarden(){
        return follow(follower, intake13S())
                .with(robot.getIntake().in()).with(robot.getTransfer().in()
                        .then(follow(follower, volta3S()))
                        .then(follow(follower, bate3S()))
                        .then(robot.getIntake().in()).with(robot.getTransfer().in()));
    }


}
