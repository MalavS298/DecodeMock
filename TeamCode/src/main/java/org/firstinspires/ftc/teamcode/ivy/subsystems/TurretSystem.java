package org.firstinspires.ftc.teamcode.ivy.subsystems;

import static org.firstinspires.ftc.robotcore.internal.system.Finalizer.getTag;

import android.util.Size;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class TurretSystem {

    private DcMotorEx turretm;

    private double kP = 0.0140;
    private double kD = 0.0080;
    private double goalX = 0;
    private double lastError = 0;
    private double angleTolerance = 0.2;
    private final double MAX_POWER = 0.7;
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();

    /** April Tag Webcam **/
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public TurretSystem(HardwareMap hardwareMap){
        turretm=hardwareMap.get(DcMotorEx.class, "turret");
        turretm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        /** April Tag Webcam **/
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class,"webcam"));
        builder.setCameraResolution(new Size(640 , 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    public void resetTimer(){
        timer.reset();
    }

    // Identify the tag which is locked into
    public AprilTagDetection getTag(int id) {
        for (AprilTagDetection detection : aprilTagProcessor.getDetections()) {
            if (detection.id == id) {
                return detection;
            }
        }
        return null;
    }

    public AprilTagDetection getTagBySpecific(int id) {
        for (AprilTagDetection detection : aprilTagProcessor.getDetections()) {
            if (detection.id == id) {
                return detection;
            }
        }
        return null;
    }
    public void update() {

        AprilTagDetection curID = getTagBySpecific(20);

        double deltaTime = timer.seconds();
        timer.reset();

        // Tag 20 NOT FOUND → keep searching
        if (curID == null) {
            turretm.setPower(0.8);
            return;
        }

        // Tag 20 FOUND → track it
        double error = goalX - curID.ftcPose.bearing;
        double pTerm = error * kP;

        double dTerm = 0;
        if (deltaTime > 0) {
            dTerm = ((error - lastError) / deltaTime) * kD;
        }

        if (Math.abs(error) < angleTolerance) {
            power = 0;
        } else {
            power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
        }

        turretm.setPower(power);
        lastError = error;
    }


    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }


}
