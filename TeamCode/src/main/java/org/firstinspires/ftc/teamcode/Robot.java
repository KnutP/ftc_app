package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Robot {

    private DcMotor lDriveM, rDriveM, lLift, rLift, extension, intake, pivot;
    private Servo hopper;

    private HardwareMap hwMap;

    OpMode opMode;


    public void init(HardwareMap ahwMap, OpMode op) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = op;

        this.hopper = hwMap.get(Servo.class, "hopper");

        // Define and Initialize Motors
        this.lLift = hwMap.get(DcMotor.class, "lLift");
        this.rLift = hwMap.get(DcMotor.class, "rLift");
        this.extension = hwMap.get(DcMotor.class, "extension");
        this.intake = hwMap.get(DcMotor.class, "intake");
        this.pivot = hwMap.get(DcMotor.class, "pivot");

        this.lDriveM  = hwMap.get(DcMotor.class, "LD");
        this.rDriveM = hwMap.get(DcMotor.class, "RD");
        lDriveM.setDirection(DcMotor.Direction.REVERSE);
        rDriveM.setDirection(DcMotor.Direction.FORWARD);

        stop();

        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void setIntakePower(double intakePower){
        intake.setPower(intakePower);
    }

    public void setLiftPower(double liftPower){
        lLift.setPower(liftPower);
        rLift.setPower(-liftPower);
    }

    public void setExtensionPower(double extensionPower){
        extension.setPower(extensionPower);
    }

    public void setPivotPower(double pivotPower){
        pivot.setPower(pivotPower);
    }

    public void setHopperPosition(double hopperPosition){
        hopper.setPosition(hopperPosition);
    }

    public void drive(double lPower, double rPower){
        lDriveM.setPower(lPower);
        rDriveM.setPower(rPower);
    }


    public void stop(){
        lDriveM.setPower(0);
        rDriveM.setPower(0);
    }

    public void setMotorMode(DcMotor.RunMode mode) {
        rDriveM.setMode(mode);
        lDriveM.setMode(mode);
    }
}
