package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HoodAngle {

    public enum State {CLOSE, FAR};
    private final Servo hood_angle;
    private State state = State.CLOSE;

    public HoodAngle(HardwareMap hardwareMap){
        hood_angle = hardwareMap.get(Servo.class, "hood_angle");
    }



    public void setState (State newState){
        state = newState;
        switch (newState){
            case CLOSE:
                hood_angle.setPosition(0.65);
                break;
            case FAR:
                hood_angle.setPosition(0.2);
                break;
        }

    }

    public Command rest(){return instant(() -> setState(State.CLOSE)).requiring(this);}

    public Command angle(){return instant(() -> setState(State.FAR)).requiring(this);}
}
