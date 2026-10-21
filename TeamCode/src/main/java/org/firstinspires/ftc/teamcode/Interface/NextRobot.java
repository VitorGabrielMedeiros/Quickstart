package org.firstinspires.ftc.teamcode.Interface;

import java.util.Collections;
import java.util.Set;

import dev.nextftc.robot.Mechanism;

public interface NextRobot {
    default void periodic() {}
    default Set<Mechanism> getMechanisms() { return Collections.emptySet(); }
}
