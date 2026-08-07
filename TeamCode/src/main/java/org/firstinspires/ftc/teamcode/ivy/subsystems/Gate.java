package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gate {

    public enum State {CLOSE, OPEN};
    private final Servo gate;
    private State state = State.CLOSE;
    public Gate(HardwareMap hardwareMap){
        gate = hardwareMap.get(Servo.class, "gate");
    }



    public void setState (State newState){
        state = newState;
        switch (newState){
            case CLOSE:
                gate.setPosition(0);
                break;
            case OPEN:
                gate.setPosition(0.5);
        }

    }

    public Command cl(){return instant(() ->setState(State.OPEN)).requiring(this);}

    public Command op(){
        return instant(() -> setState(State.OPEN)).requiring(this);
    }
}
