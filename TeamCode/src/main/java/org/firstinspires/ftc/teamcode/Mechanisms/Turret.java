
package org.firstinspires.ftc.teamcode.Mechanisms;

//import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

//@Configurable
public class Turret implements Mechanism {

    private final NextServo servo1 = new NextServo("servo1");
    private final NextServo servo2 = new NextServo("servo2");

    // Calibracao inicial
//    public static double SERVO1_CENTER = 0.50;
//    public static double SERVO2_CENTER = 0.50;
//
//    public static double SERVO1_RANGE = 0.50;
//    public static double SERVO2_RANGE = 0.50;

    // Limite angular inicial
    public static double MAX_ANGLE_DEG = 180.0;

    // Sentido dos servos
//    public static double SERVO1_DIRECTION = 1.0;
//    public static double SERVO2_DIRECTION = -1.0;

    double turretServo;
    private double robotX;
    private double robotY;
    private double targetX;
    private double targetY;
    private double heading;

    public double lastServoPos;
    private double fieldAngle;
    private double turretAngle;
    private double posServo;
    private double posServoGraus;


    private double calculateAngle(double robotX, double robotY, double xHive, double yHive, double heading) {

        double dx = xHive - robotX;
        double dy = yHive - robotY;

        fieldAngle = Math.atan2(dy, dx);
        //posServoGraus = (Math.toDegrees(heading) - turretAngle);
        turretAngle = ((Math.toDegrees(fieldAngle) - heading) + 180);
        return turretAngle;
    }

    public double conversor(double degrees){
        posServo =  0.00241667 * degrees +0;
        return posServo;
    }

    public double conversorServoToGraus (double servo){
        posServoGraus = 181.4 * servo + 181.4;
        return posServoGraus;
    }

    public void moveServos(double robotX, double robotY, double xHive, double yHive, double heading) {

        turretServo = conversor(calculateAngle(robotX, robotY, xHive, yHive, heading));

        //double normalizedAngle = turretServo / MAX_ANGLE_DEG;

//        double position1 = SERVO1_CENTER + SERVO1_DIRECTION * normalizedAngle * SERVO1_RANGE;
//
//        double position2 = SERVO2_CENTER + SERVO2_DIRECTION * normalizedAngle * SERVO2_RANGE;

        // Mantem os comandos dentro de 0.0 a 1.0
//        position1 = clamp(position1, 0.0, 1.0);
//        position2 = clamp(position2, 0.0, 1.0);

        //turretServo = clamp(turretServo, 0 ,1.0);
    }
//    private double normalizeAngle(double angle) {
//
//        while (angle > Math.PI) {
//            angle -= 2.0 * Math.PI;
//        }
//
//        while (angle < -Math.PI) {
//            angle += 2.0 * Math.PI;
//        }
//
//        return angle;
//    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public double getTurretAngleDegrees() {
        return turretAngle;
    }
    public double getTheta() {
        return fieldAngle;
    }

    public double getFieldAngleDegrees() {
        return Math.toDegrees(fieldAngle);
    }

    public double getTurretServo() {
        return turretServo;
    }


    @Override
    public void periodic() {

    }


}
