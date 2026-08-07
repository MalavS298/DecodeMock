package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ivy.subsystems.Gate;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Intake;

@TeleOp(name = "DecodeMockTeleOp")

public class teleop extends OpMode {
    private Intake intake;

    private Gate gate;
    private Intake index;
    DcMotor leftFront, rightFront, leftBack, rightBack;
    public double SpeedMultiplier = 0.65;

    @Override
    public void init() {

        /** Mec Init **/
        leftFront=hardwareMap.get(DcMotor.class, "fl");
        rightFront=hardwareMap.get(DcMotor.class,"fr");
        leftBack=hardwareMap.get(DcMotor.class,"bl");
        rightBack=hardwareMap.get(DcMotor.class,"br");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        /** Intake Init **/
        intake = new Intake(hardwareMap);
        index = new Intake(hardwareMap);

        /** Gate init **/
        gate = new Gate(hardwareMap);

    }

    @Override
    public void loop() {

        /** ---------------- Mecanum ---------------- **/
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        double leftFrontPower = drive+strafe+turn;
        double rightFrontPower= drive-strafe-turn;
        double leftBackPower = drive-strafe+turn;
        double rightBackPower = drive+strafe-turn;

        double max = Math.max(
                Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)),
                Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower))
        );

        if(max > 1.0){
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        leftFront.setPower(leftFrontPower*SpeedMultiplier);
        rightFront.setPower(rightFrontPower*SpeedMultiplier);
        leftBack.setPower(leftBackPower*SpeedMultiplier);
        rightBack.setPower(rightBackPower*SpeedMultiplier);


        /** ---------------- Intake ---------------- **/
        if(gamepad1.left_bumper){
            intake.in();
        } else if (gamepad1.right_bumper) {
            intake.idle();
        } else if (gamepad1.left_bumper && gamepad1.right_bumper) {
            intake.out();
        }

        /** ---------------- Gate and Shooter ---------------- **/
         if (gamepad1.left_trigger > 0.5) {
            gate.op();
        }
         else {
             gate.cl();
         }


    }
}
