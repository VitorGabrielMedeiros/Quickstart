package org.firstinspires.ftc.teamcode.Mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Intake implements Mechanism {

    private final NextMotor motor = new NextMotor("intake");

    public Command in() {
        return infinite(() -> motor.setThrottle(1.0));
    }

    public Command out() {
        return infinite(() -> motor.setThrottle(-1.0));
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
}


