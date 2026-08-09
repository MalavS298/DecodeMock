package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake_Transfer {

    public enum State {INTAKE, OUTTAKE, IDLE};

    private final DcMotorEx intake;
    private final DcMotorEx index;
    private State state = State.IDLE;
    public Intake_Transfer(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        index = hardwareMap.get(DcMotorEx.class,"index");
        index.setDirection(DcMotorSimple.Direction.FORWARD);
        index.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        index.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setState (State newState){
        state = newState;
        switch (newState){
            case INTAKE:
                intake.setPower(1.0);
                index.setPower(1.0);
                break;
            case OUTTAKE:
                intake.setPower(-1.0);
                index.setPower(-1.0);
                break;
            case IDLE:
                intake.setPower(0);
                index.setPower(0);
                break;
        }
    }

    public Command in(){
        return instant(() -> setState(State.INTAKE)).requiring(this);
    }
    public Command out(){
        return instant(() -> setState(State.OUTTAKE)).requiring(this);
    }
    public Command idle(){
        return instant(() -> setState(State.IDLE)).requiring(this);
    }
}
