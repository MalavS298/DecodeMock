package org.firstinspires.ftc.teamcode.ivy.autonomous;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Command;

import org.firstinspires.ftc.teamcode.ivy.subsystems.Intake_Transfer;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;

public class blueClose extends OpMode {

    private Intake_Transfer intake;
    private Shooter shooter;
    private Follower follower;
    private CommandBuilder auto;

    /** ------- Establish Paths */
    public PathChain Shoot1;
    public PathChain Intake2;
    public PathChain Shoot2;
    public PathChain Intake3;
    public PathChain Shoot3;

    @Override
    public void init() {
        intake = new Intake_Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        new Pose(20.074, 120.407, Math.toRadians(142));

        /** ------- Paths */
        Shoot1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(20.074, 120.407),
                                new Pose(51.592, 89.999)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(142))
                .build();

        Intake2 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(51.592, 89.999),
                                new Pose(60.049, 80.331),
                                new Pose(15.685, 82.532)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(142), Math.toRadians(180))
                .build();

        Shoot2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(15.685, 82.532),
                                new Pose(51.803, 89.924)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(142))
                .build();

        Intake3 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(51.803, 89.924),
                                new Pose(65.460, 56.203),
                                new Pose(14.909, 58.888)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(142), Math.toRadians(180))
                .build();

        Shoot3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(14.909, 58.888),
                                new Pose(52.289, 90.154)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(142))
                .build();

        /**---- Actuall Auto */
        auto = sequential(
                // Shoot Set #1
                follow(follower, Shoot1),
                shooter.czone(),
                waitMs(2000),
                shooter.off(),
                parallel(
                        // Intake Set #2
                        follow(follower, Intake2),
                        intake.in()
                ),
                //Shoot Set #2
                intake.idle(),
                follow(follower, Shoot2),
                shooter.czone(),
                waitMs(2000),
                shooter.off(),
                parallel(
                        // Intake Set #3
                        follow(follower, Intake3),
                        intake.in()
                ),
                //Shoot Set #3
                intake.idle(),
                follow(follower, Shoot3),
                shooter.czone(),
                waitMs(2000),
                shooter.off()

        );

    }
    @Override
    public void start(){
        auto.schedule();
    }

    @Override
    public void loop() {
        Scheduler.execute();
    }
}
