
package org.firstinspires.ftc.teamcode.Mechanisms;

//import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

//@Configurable
public class Turret implements Mechanism {

    private final NextServo servo1 = new NextServo("servo1");
    private final NextServo servo2 = new NextServo("servo2");

    // Calibracao inicial
    public static double SERVO1_CENTER = 0.50;
    public static double SERVO2_CENTER = 0.50;

    public static double SERVO1_RANGE = 0.50;
    public static double SERVO2_RANGE = 0.50;

    // Limite angular inicial
    public static double MAX_ANGLE_DEG = 180.0;

    // Sentido dos servos
    public static double SERVO1_DIRECTION = 1.0;
    public static double SERVO2_DIRECTION = -1.0;

    private double robotX;
    private double robotY;
    private double targetX;
    private double targetY;
    private double heading;

    private double fieldAngle;
    private double turretAngle;

    public void teste(
            double x,
            double y,
            double xHive,
            double yHive,
            double heading) {

        this.robotX = x;
        this.robotY = y;
        this.targetX = xHive;
        this.targetY = yHive;
        this.heading = heading;

        calculateAngle();
        //moveServos();
    }

    private void calculateAngle() {

        double dx = targetX - robotX;
        double dy = targetY - robotY;

        // Direcao do robo ate o alvo no campo
        fieldAngle = Math.atan2(dy, dx);

        // Converte para o referencial do robo
        turretAngle = normalizeAngle(fieldAngle - heading);

        // Limita a rotacao solicitada
        double maxAngle = Math.toRadians(MAX_ANGLE_DEG);

        turretAngle = Math.max(
                -maxAngle,
                Math.min(maxAngle, turretAngle)
        );
    }

    /*private void moveServos() {

        double angleDegrees = Math.toDegrees(turretAngle);

        double normalizedAngle = angleDegrees / MAX_ANGLE_DEG;

        double position1 = SERVO1_CENTER + SERVO1_DIRECTION * normalizedAngle * SERVO1_RANGE;

        double position2 = SERVO2_CENTER + SERVO2_DIRECTION * normalizedAngle * SERVO2_RANGE;

        // Mantem os comandos dentro de 0.0 a 1.0
        position1 = clamp(position1, 0.0, 1.0);
        position2 = clamp(position2, 0.0, 1.0);

        servo1.setPosition(position1);
        servo2.setPosition(position2);
    }

     */

    private double normalizeAngle(double angle) {

        while (angle > Math.PI) {
            angle -= 2.0 * Math.PI;
        }

        while (angle < -Math.PI) {
            angle += 2.0 * Math.PI;
        }

        return angle;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public double getTurretAngleDegrees() {
        return Math.toDegrees(turretAngle);
    }

    public double getFieldAngleDegrees() {
        return Math.toDegrees(fieldAngle);
    }


}
