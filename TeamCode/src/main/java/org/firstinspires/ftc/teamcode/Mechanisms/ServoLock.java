package org.firstinspires.ftc.teamcode.Mechanisms;


import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class ServoLock implements Mechanism {

    private final NextServo servo = new NextServo("lock");

    private static final double LOCK_POSITION = 0.0;
    private static final double UNLOCK_POSITION = 1.0;

    public Command lock() {
        return instant(() -> servo.setPosition(LOCK_POSITION)); // alterar valor para o correto
    }

    public Command unlock() {
        return instant(() -> servo.setPosition(UNLOCK_POSITION)); // alterar valor para o correto
    }
}


