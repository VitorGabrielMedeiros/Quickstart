package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.opmode.NextTeleop;

@Config
@NextTeleop
public class Flowertake implements Mechanism {

    private final NextServo flower1 = new NextServo("flower1");
    private final NextServo flower2 = new NextServo("flower2");
    public static double potenciaFlower = 0.5;
    public static double potenciaShoot = 0.10;
    public static double potenciaDefense = 0.18;
    public static double potenciaHigh = 0.0;

    //flower2
//    public static double potenciaSHigh2 = 0.5;
//    public static double potenciaShoot1 = 0.10;
//    public static double potenciaDefense1 = 0.18;
//    public static double potenciaSFlower2 = 0.0;
    public  Command levantado() {return  infinite(() -> flower2.setPosition((potenciaHigh)));}
    public Command flower() {return infinite(() -> flower2.setPosition(potenciaFlower));}
    public Command ServoShoot() {return infinite(() -> flower2.setPosition(potenciaShoot));}
    public Command defense() {return infinite(() -> flower2.setPosition(potenciaDefense));}

    public Command levantado1() {return  infinite(() -> flower1.setPosition(1-(potenciaHigh)));}
    public Command flower1() {return infinite(() -> flower1.setPosition(1-(potenciaFlower)));}
    public Command ServoShoot1() {return infinite(() -> flower1.setPosition(1-(potenciaShoot)));}
    public Command defense1() {return infinite(() -> flower1.setPosition(1-(potenciaDefense)));}



    @Override
    public void periodic(){
    }

}
