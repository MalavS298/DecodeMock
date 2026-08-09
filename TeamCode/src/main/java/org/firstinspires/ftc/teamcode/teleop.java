package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ivy.subsystems.Shooter.*;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Gate;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Intake_Transfer;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Light;
import org.firstinspires.ftc.teamcode.ivy.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.ivy.subsystems.TurretSystem;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "DecodeMockTeleOp")

public class teleop extends OpMode {
    /** Intake Define **/
    private Intake_Transfer intake;

    /** Light Define **/
    private Light light;

    /** Gate Define **/
    private Gate gate;

    /** Mec Define **/
    DcMotor leftFront, rightFront, leftBack, rightBack;
    public double SpeedMultiplier = 0.65;

    /** Color Define **/
    public NormalizedColorSensor colorSensor;

    /** Distance Define **/
    public DistanceSensor distanceSensor;

    /** Shooter Define **/
    private Shooter flyWheel;
    private Shooter flyWheel2;

    /** April Tag Define **/
    private TurretSystem turret;


    @Override
    public void init() {

        /** Mec Init **/
        leftFront = hardwareMap.get(DcMotor.class, "fl");
        rightFront = hardwareMap.get(DcMotor.class, "fr");
        leftBack = hardwareMap.get(DcMotor.class, "bl");
        rightBack = hardwareMap.get(DcMotor.class, "br");


        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        /** Intake Init **/
        intake = new Intake_Transfer(hardwareMap);

        /** Gate init **/
        gate = new Gate(hardwareMap);

        /** Light init **/
        light = new Light(hardwareMap);

        /**  Color init**/
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "revColorV3");
        colorSensor.setGain(11);

        /**  Distance init**/
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");

        /**  April Tag init**/
        turret = new TurretSystem(hardwareMap);

        /** Shooter init **/
        flyWheel = new Shooter(hardwareMap);

    }

    public void start(){
        turret.resetTimer();
    }

    @Override
    public void loop() {
        /** ---------------- Turret ---------------- **/
        turret.update();
        double tagdistance = turret.distance();

        if (gamepad1.right_trigger > 0.5) {
            if (tagdistance <= 50) {
                flyWheel.czone().schedule();
            } else {
                flyWheel.fzone().schedule();
            }
        }

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
            intake.in().schedule();
        } else if (gamepad1.right_bumper) {
            intake.idle().schedule();
        } else if (gamepad1.left_bumper && gamepad1.right_bumper) {
            intake.out().schedule();
        }

        /** ---------------- Gate & Shooter ---------------- **/
         if (gamepad1.left_trigger > 0.5) {
            gate.op().schedule();
        }
         else {
             gate.cl().schedule();
         }
        /** ---------------- Color & Light ---------------- **/
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        int col = colors.toColor();
        double hue = JavaUtil.colorToHue(col);

        if (hue > 151 && hue < 170) {
            light.GR().schedule();
        } else if (hue > 205 && hue < 225) {
            light.PU().schedule();
        }else{
            light.OFF().schedule();
        }

        /** ---------------- Distance ---------------- **/
        double distance = distanceSensor.getDistance(DistanceUnit.INCH);
        if (distance <= 2.5) {
            telemetry.addLine("Balls Loaded!");
        }
        else {
            telemetry.addLine("Balls Not Loaded!");
        }

        Scheduler.execute();
    }
}
