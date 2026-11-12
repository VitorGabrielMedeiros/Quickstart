package org.firstinspires.ftc.teamcode.Mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Intake implements Mechanism {

    public double velshot;

    private final NextMotor motor = new NextMotor("intake");

    public double getVel(){
        double vel = motor.getEncoderVelocity().getMagnitude();
        return vel;
    }

    public Command shoot(){
        return instant(() -> motor.setThrottle(velshot));
    }

    public Command in() {
        return instant(() -> motor.setThrottle(-1.0));
    }

    public Command out() {return instant(() -> motor.setThrottle(1.0));
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
    public Command NoFivePolen(){return instant(() -> motor.setThrottle(1));}

    public void setVel(double vel){
        this.velshot = vel;
    }
}




