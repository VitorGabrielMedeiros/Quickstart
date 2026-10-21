
package org.firstinspires.ftc.teamcode.TeleOp;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode {

    private final Robot robot;

    public Follower follower;
    public final PoseFactory start = PoseFactory.radians();
    private final Pose startPose = start.of(144,0,90);

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
        if (yRobot > 72) {
            xHive = 84;
            yHive = 86;
        } else {
            xHive = 84;
            yHive = 55;
        }

        // Envia os dados para a turret calcular a mira
        robot.getTurret().teste(
                xRobot,
                yRobot,
                xHive,
                yHive,
                heading
        );

        Telemetry.log("Status", "Running");
        Telemetry.log("Robot X", xRobot);
        Telemetry.log("Robot Y", yRobot);
        Telemetry.log("Heading", Math.toDegrees(heading));
        Telemetry.log("Turret Angle", robot.getTurret().getTurretAngleDegrees());

        Telemetry.update();
    }

    @Override
    public void start() {

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        robot.drivetrain.startDrive(gamepad1, follower);
    }
}
