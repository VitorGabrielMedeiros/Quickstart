package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.pedropathing.ivy.groups.Groups.loop;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.colors.ColorProfile;
import dev.nextftc.hardware.sensors.colors.ColorSpace;
import dev.nextftc.hardware.sensors.colors.NextColor;
import dev.nextftc.robot.Mechanism;

public class Transfer implements Mechanism {

    public double vel;



    private final NextMotor motor = new NextMotor("transfer");
    private final NextColorDistanceSensor SensorC = new NextColorDistanceSensor("sensorC");

    public Command inC(double color) {
        if (color > 0.6) {
            return infinite(() -> motor.setThrottle(0.0));
        } else {
            return infinite(() -> motor.setThrottle(1.0));
        }

    }

    public void setPower(double power){
        motor.setThrottle(power);
    }

    public Command shoot(){
        return infinite(() -> motor.setThrottle(vel));
    }
    public  Command in(){
        return  instant( () -> motor.setThrottle(1.0));
    }


    public Command out() {
        return instant(() -> motor.setThrottle(-1.0));
    }

    public Command stop() {
        return instant(() -> motor.setThrottle(0.0));
    }
    public Command NoFivePolen(){return instant(() -> motor.setThrottle(-1));}

    public NextColor getsensorC(){
        return SensorC.getColor();
    }

    public void setVel(double vel){
        this.vel = vel;
    }

    @Override
    public void periodic() {



    }
}


