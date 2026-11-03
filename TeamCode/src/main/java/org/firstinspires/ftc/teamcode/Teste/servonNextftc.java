package org.firstinspires.ftc.teamcode.Teste;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Robot;

import dev.nextftc.hardware.actuators.NextFeedbackServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.hardware.util.AnalogFeedback;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
@Config
@NextTeleop
public class servonNextftc extends NextOpMode {

    private final Robot robot;

    NextFeedbackServo servo = new NextFeedbackServo("servo", new NextAnalogInput("armEncoder"));

    public servonNextftc(Robot robot){
        super(robot);
        this.robot = robot;
    }

    @Override
    public void periodic(){
        servo.setPosition(0.5);

        double anguloRad = servo.getAngle().getMagnitude();
        double anguloGraus = Math.toDegrees(anguloRad);

    }

    @Override
    public void start(){

    }

}
