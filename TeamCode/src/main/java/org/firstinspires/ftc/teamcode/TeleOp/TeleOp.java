
package org.firstinspires.ftc.teamcode.TeleOp;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Mechanisms.Turret;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.qualcomm.robotcore.hardware.Servo;
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

    private double xHive;
    private double yHive;
    public double lastHeading;
    public double voltas;
    public double delta;
    public double anguloTotal;
    CommandGamepad gp2 = new CommandGamepad(gamepad2);


    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;
    }


    @Override
    public void periodic() {


        // Atualiza a estimativa de pose do Pedro
        follower.update();

        // Le a posicao e a orientacao do robo
        xRobot = follower.pose().x();
        yRobot = follower.pose().y();
        heading = Math.toDegrees(follower.pose().heading());

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

        Telemetry.update();

        lastHeading = heading;
    }

    @Override
    public void start() {
        robot.getLock().lock().start();

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        robot.drivetrain.startDrive(gamepad1, follower);


        gp2.leftBumper().whileTrue(robot.getIntake().in().with(robot.getTransfer().in()));
        gp2.leftBumper().whileFalse(robot.getIntake().stop().with(robot.getTransfer().stop()));

        gp2.y().whileTrue(robot.getIntake().out());
        gp2.y().whileFalse(robot.getIntake().stop());



    }
}
