package org.firstinspires.ftc.teamcode.Mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class Flowertake implements Mechanism {

    private final NextServo flower1 = new NextServo("flower1");
    private final NextServo flower2 = new NextServo("flower2");
    public Command abaixado() {return  infinite(() -> flower1.setPosition(1.0));}
    public Command levantado() {return infinite(() -> flower2.setPosition(0.0));}
    public Command defense() {return infinite(() -> flower1.setPosition(0.5));}
    public Command abaixado2() {return  infinite(() -> flower2.setPosition(1.0));}
    public Command levantado2() {return infinite(() -> flower2.setPosition(0.0));}
    public Command defense2() {return infinite(() -> flower1.setPosition(0.5));}
}
