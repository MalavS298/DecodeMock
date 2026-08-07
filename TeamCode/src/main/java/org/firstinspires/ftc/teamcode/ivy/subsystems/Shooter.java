package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {

    public enum State {CLOSE, FAR};
    private final Servo hood_angle;
    private final DcMotorEx flyWheel;
    private State state = State.CLOSE;

    public Shooter(HardwareMap hardwareMap){
        hood_angle = hardwareMap.get(Servo.class, "hood_angle");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        flyWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }



    public void setState (State newState){
        state = newState;
        switch (newState){
            case CLOSE:
                hood_angle.setPosition(0.65);
                flyWheel.setPower(1600);
                break;
            case FAR:
                hood_angle.setPosition(0.2);
                flyWheel.setVelocity(1900);
                break;
        }

    }

    public Command rest(){return instant(() -> setState(State.CLOSE)).requiring(this);}

    public Command angle(){return instant(() -> setState(State.FAR)).requiring(this);}
}
