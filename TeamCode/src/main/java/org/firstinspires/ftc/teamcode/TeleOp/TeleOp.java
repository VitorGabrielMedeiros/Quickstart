
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
        heading = follower.pose().heading();

        // Escolhe o alvo
        if (yRobot > 70.5) {
            xHive = 84;
            yHive = 86;
        } else {
            xHive = 84;
            yHive = 55;
        }

        robot.getTurret().moveServos(xRobot, yRobot, xHive, yHive, heading);

        servo1.setPosition(1-(robot.getTurret().getTurretServo()));
        servo2.setPosition(1- (robot.getTurret().getTurretServo()));

        Telemetry.log("Status", "Running");
        Telemetry.log("Robot X", xRobot);
        Telemetry.log("Robot Y", yRobot);
        Telemetry.log("servoPos", robot.getTurret().getTurretServo());
        Telemetry.log("theta" , robot.getTurret().getFieldAngleDegrees());
        Telemetry.log("Heading", Math.toDegrees(heading));
        Telemetry.log("Turret Angle", robot.getTurret().getTurretAngleDegrees());

        Telemetry.update();
    }

    @Override
    public void start() {
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        robot.drivetrain.startDrive(gamepad1, follower);
    }
}
