
package org.firstinspires.ftc.teamcode.Auto;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextAutonomous(name = "Auto")
public class Auto extends NextOpMode {

    private  final NextColorDistanceSensor sensorC = new NextColorDistanceSensor("sensorC");

    private final Robot robot;
    protected Follower follower;
    public final PoseFactory start = PoseFactory.radians();
    private final Pose startPose = start.of(133.34375,7.5,Math.toRadians(90));
    public Pose poseReset = new Pose (106, 7.5, Math.toRadians(90));
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
    Command delayServo = waitMs(100);


    public static double posMaior = 2020;
    public static double posMenor = 500;
    public double autoshoot(double distancia){
        flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
        return -flywheel;
    }


    public Auto(Robot robot) {
        super(robot);
        this.robot = robot;
    }

    protected Command auto(){
        return sequential(
                delay,
                robot.getIntake().in().with(robot.getTransfer().in().with(robot.getLock().unlock())),
                delay,
                robot.getIntake().stop().with(robot.getTransfer().stop().with(robot.getLock().lock()))

        );
    }

    @Override
    public void disabledPeriodic(){
        if(follower == null){
            Scheduler.reset();
            follower = Constants.create(hardwareMap);
            follower.setPose(startPose);
        }

        follower.update();
    }

    @Override
    public void start(){
        schedule(auto());
    }


    @Override
    public void periodic() {


        // Atualiza a estimativa de pose do Pedro
        follower.update();
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

        sensorC.update();

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
        Telemetry.log("SensorC", sensorC.getColor());
        Telemetry.log("Follower Mode", follower.mode());


        Telemetry.update();

        lastHeading = heading;
    }

}

//ola

