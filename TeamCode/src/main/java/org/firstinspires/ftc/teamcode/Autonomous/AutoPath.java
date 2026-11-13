package org.firstinspires.ftc.teamcode.Autonomous;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.bateIntakeG3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.flower3S1;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.flower3S1Curve;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.flower3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.flowerLinha;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.goFlower3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.goIntakeA3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.intakeA3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.intakeG3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.intakeGCurve3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.park3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S1;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S1Curve;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S2;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S2Curve;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S3;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.shoot3S4;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.startPose3S;
import static org.firstinspires.ftc.teamcode.Autonomous.AutoPose.voltaIntakeg3S;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;

public class AutoPath  {

    private Follower follower;

    // ---AUTO 3 TIP SOLO---
    public static final Path intake13S() {
        return curve(startPose3S,intakeGCurve3S,intakeG3S).linear(startPose3S, intakeG3S);
    }
    public static final Path volta3S() {
        return line(intakeG3S,voltaIntakeg3S).linear(intakeG3S, voltaIntakeg3S);
    }
    public static final Path bate3S() {
        return line(voltaIntakeg3S,bateIntakeG3S).linear(voltaIntakeg3S, bateIntakeG3S);
    }
    public static final Path goShoot3S1() {
        return curve(bateIntakeG3S,shoot3S1Curve,shoot3S1).linear(bateIntakeG3S, shoot3S1);
    }
    public static final Path goFlower3S1() {
        return line(shoot3S1,flowerLinha).linear(shoot3S1, flowerLinha);
    }
    public static final Path flowerIntakeNovo() {
        return line(flowerLinha, flower3S1).linear(flowerLinha, flower3S1);
    }
    public static final Path goShoot3S2() {
        return curve(flower3S1,shoot3S2Curve,shoot3S2).linear(flower3S1, shoot3S2);
    }
    public static final Path goIntakeM3S() {
        return line(shoot3S2,goIntakeA3S).linear(shoot3S2, goIntakeA3S);
    }
    public static final Path intakeM3S() {
        return line(goIntakeA3S,intakeA3S).linear(goIntakeA3S, intakeA3S);
    }
    public static final Path goShoot3S3() {
        return line(intakeA3S,shoot3S3).linear(intakeA3S, shoot3S3);
    }
    public static final Path vaiFlower3S2() {
        return line(shoot3S3,goFlower3S2).linear(shoot3S3, goFlower3S2);
    }
    public static final Path intakeFlower3S2() {
        return line(goFlower3S2,flower3S2).linear(goFlower3S2, flower3S2);
    }
    public static final Path goShoot3S4() {
        return line(flower3S2,shoot3S4).linear(flower3S2, shoot3S4);
    }
    public static final Path pathPark3S() {
        return line(flower3S1,park3S).linear(flower3S1,park3S);
    }

    // ---AUTO 2 TIP SOLO---


    // ---AUTO 3 TIP ALLIANCE---


    // ---AUTO ALLIANCE---


}
