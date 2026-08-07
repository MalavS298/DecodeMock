package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Light {
    public enum State {GR, PU, OFF};
    private final Servo light;
    private State state = State.OFF;

    public Light(HardwareMap hardwareMap){
        light = hardwareMap.get(Servo.class, "light");
    }

    public void setState(State newstate) {
        state = newstate;
        switch (newstate) {
            case GR:
                light.setPosition(0.5);
                break;
            case PU:
                light.setPosition(0.67);
                break;
        }
    }

    public Command GR(){
        return instant(() -> setState(Light.State.GR)).requiring(this);
    }

    public Command PU(){
        return instant(() -> setState(Light.State.PU)).requiring(this);
    }
    public Command OFF(){return instant(() -> setState(Light.State.OFF)).requiring(this);}

}
