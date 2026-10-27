//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Pose;
//
//import org.firstinspires.ftc.teamcode.Robot;
//
//import dev.nextftc.robot.opmode.NextOpMode;
//
//public class Interpolation extends NextOpMode {
//
//    private final Robot robot;
//    public Follower follower;
//    public double potenciaFlywheel;
//    public final PoseFactory distanceGoal = PoseFactory.radians();
//    private final Pose goal = distanceGoal.of(84,55,0);
//
//
//
//    public Interpolation(Robot robot){
//        super(robot);
//        this.robot = robot;
//    }
//    public double inter(double distance){
//        distance = follower.pose().distance(goal);
//        potenciaFlywheel = 6 * distance + 1140;
//        return potenciaFlywheel;
//    }
//
//    @Override
//    public void periodic(){
//
//
//
//
//    }
//
//    @Override
//    public void start(){
//
//
//    }
//
//
//}
