package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    public enum State {CLOSE, FAR, OFF};
    private final DcMotorEx flyWheel;
    private final DcMotorEx flyWheel2;
    private final Servo hood_angle;
    private final DcMotorEx index;
    private State state = State.CLOSE;
    public static double lowVelocity = 1600;
    public static double highVelocity = 1900;
    public static double F = 0;
    public static double P = 0;
    public static double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    public static int stepIndex = 1;
    public static double curTargetVelocity = highVelocity;


    public Shooter(HardwareMap hardwareMap){
        hood_angle = hardwareMap.get(Servo.class, "hood_angle");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        flyWheel2 = hardwareMap.get(DcMotorEx.class, "flyWheel2");
        flyWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel.setVelocityPIDFCoefficients(P, 0, 0, F);
        flyWheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        flyWheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel2.setVelocityPIDFCoefficients(P, 0, 0, F);
        index = hardwareMap.get(DcMotorEx.class,"index");
        index.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPIDF(double p, double f) {
        flyWheel.setVelocityPIDFCoefficients(p, 0, 0, f);
        flyWheel2.setVelocityPIDFCoefficients(p, 0, 0, f);
    }


    public void setState (State newState){
        state = newState;
        switch (newState){
            case CLOSE:
                hood_angle.setPosition(0.65);
                setPIDF(0.011, 0.008);
                flyWheel.setVelocity(lowVelocity);
                flyWheel2.setVelocity(lowVelocity);
                index.setPower(1.0);
                break;
            case FAR:
                hood_angle.setPosition(0.2);
                setPIDF(0.013, 0.015);;
                flyWheel.setVelocity(highVelocity);
                flyWheel2.setVelocity(highVelocity);
                index.setPower(1.0);
                break;
            case OFF:
                flyWheel.setVelocity(0);
                flyWheel2.setVelocity(0);
                index.setPower(0);
        }

    }

    public Command czone(){return instant(() -> setState(State.CLOSE)).requiring(this);}
    public Command fzone(){return instant(() -> setState(State.FAR)).requiring(this);}
    public Command off(){return instant(() -> setState(State.OFF)).requiring(this);}

}