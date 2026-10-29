package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Flowertake;
import org.firstinspires.ftc.teamcode.Mechanisms.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.ServoLock;
import org.firstinspires.ftc.teamcode.Mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Turret;

import java.util.Set;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class Robot implements NextRobot {

    Intake intake = new Intake();
    public Drivetrain drivetrain = new Drivetrain();
    Flowertake flower = new Flowertake();
    ServoLock lock = new ServoLock();
    Shooter shooter = new Shooter();
    Transfer transfer = new Transfer();
    Turret turret = new Turret();
    Hood hood = new Hood();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(intake,drivetrain,flower,hood,lock,shooter,transfer,turret);
    }

    public Turret getTurret() {
        return turret;
    }
    public Hood getHood() {
        return hood;
    }

    public Shooter getShooter() {
        return shooter;
    }
    public Intake getIntake() {
        return intake;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public ServoLock getLock() {
        return lock;
    }
    public Flowertake getFlower() {
        return flower;
    }
}
