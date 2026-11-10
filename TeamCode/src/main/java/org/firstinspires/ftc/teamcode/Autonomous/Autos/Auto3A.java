//package org.firstinspires.ftc.teamcode.Autonomous.Autos;
//
//import static com.pedropathing.ivy.Scheduler.schedule;
//import static com.pedropathing.ivy.commands.Commands.waitMs;
//import static com.pedropathing.ivy.groups.Groups.sequential;
//import static com.pedropathing.ivy.pedro.PedroCommands.follow;
//
//import static org.firstinspires.ftc.teamcode.Autonomous.AutoPath.intake1S;
//import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.startPose;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.ivy.Command;
//import com.pedropathing.ivy.Scheduler;
//import com.pedropathing.math.Pose;
//import com.qualcomm.robotcore.hardware.PwmControl;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.ServoImplEx;
//
//
//import org.firstinspires.ftc.teamcode.Robot;
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//
//import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
//import dev.nextftc.robot.Telemetry;
//import dev.nextftc.robot.opmode.NextAutonomous;
//import dev.nextftc.robot.opmode.NextOpMode;
//
//@NextAutonomous (name="Auto 3 w/ Alliance")
//public class Auto3A extends NextOpMode {
//
//    private  final NextColorDistanceSensor sensorC = new NextColorDistanceSensor("sensorC");
//
//    private final Robot robot;
//    private Follower follower;
//
//    public Auto3A(Robot robot) {
//        super(robot);
//        this.robot = robot;
//    }
//
//    public Pose poseSotm;
//    private Servo servo1;
//    private Servo servo2;
//    public static double offSetShooter = 40;
//
//    private double xRobot;
//    private double yRobot;
//    private double heading;
//    public double currentDistance;
//    private double xHive = 0;
//    private double yHive = 0;
//    public double lastHeading;
//    public double voltas;
//    public double delta;
//    public double anguloTotal;
//    public double flywheel;
//    Command delay = waitMs(300);
//    Command delayServo = waitMs(100);
//
//    public static double posMaior = 2020;
//    public static double posMenor = 500;
//    public double autoshoot(double distancia){
//        flywheel = 0.199998 * Math.pow(distancia, 2) - 15.49506 * distancia + 1875.91028;
//        return (-flywheel-offSetShooter);
//    }
//
//    public double sotm(double distanciasotm){
//        return (0.000534998 * Math.pow(distanciasotm, 2)-0.0419473 * distanciasotm+1.28217);
//    }
//
//    public static double clamp (double valor, double min, double max){
//        return Math.max(min, Math.min(max, valor));
//    }
//
//
//
//    private Command auto(){
//        return sequential(
//                follow(follower, intake1S())
//        );
//    }
//
//
//
//    @Override
//    public void disabledPeriodic(){
//        Scheduler.reset();
//        follower = Constants.create(hardwareMap);
//        follower.setPose(startPose);
//        follower.update();
//    }
//
//    @Override
//    public void start(){
//        robot.getLock().unlock().schedule();
//
//        servo1 = hardwareMap.get(Servo.class, "servo1");
//        servo2 = hardwareMap.get(Servo.class, "servo2");
//
//        follower = Constants.create(hardwareMap);
//
//        follower.setPose(startPose);
//        follower.update();
//
//        ((ServoImplEx) servo1).setPwmRange(
//                new PwmControl.PwmRange(posMenor, posMaior)
//        );
//        ((ServoImplEx) servo2).setPwmRange(
//                new PwmControl.PwmRange(posMenor, posMaior)
//        );
//
//
//
//        schedule(auto());
//    }
//
//    @Override
//    public void periodic(){
//        follower.update();
//        Scheduler.execute();
//
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
//    }
//}
