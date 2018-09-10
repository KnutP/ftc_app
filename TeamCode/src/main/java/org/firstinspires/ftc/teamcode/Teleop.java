package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Ri30HTeleop", group = "Iterative Opmode")
//@Disabled
public class Teleop extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot = new Robot();
    private enum HopperPosition {Idle, up}
    private HopperPosition hopperPosition = HopperPosition.Idle;

    private boolean bPressedBefore = false;
    private boolean intakeOn= false;
    private double intakePower = 0;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot.init(hardwareMap, this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {

        // DRIVER CODE
        double lPower;
        double rPower;

        lPower = gamepad1.left_stick_y*Math.abs(gamepad1.left_stick_y);
        rPower = gamepad1.right_stick_y*Math.abs(gamepad1.right_stick_y);
        lPower = Range.clip(lPower, -1.0, 1.0);
        rPower = Range.clip(rPower, -1.0, 1.0);

        robot.drive(lPower, rPower);


        // OPERATOR CODE


        // Hopper gripper
        if(gamepad2.b && !bPressedBefore){
            if(!intakeOn ){
                intakeOn = true;
                intakePower = -1;
                bPressedBefore = true;
            }
            else{
                intakeOn = false;
                intakePower = 0;
                bPressedBefore = true;
            }
        }
        if(!gamepad2.b){
            bPressedBefore = false;
        }

        if(gamepad2.x){
            intakePower = 1;
        }
        robot.setIntakePower(intakePower);


        // Intake
        robot.setPivotPower(gamepad2.right_stick_y);
        // Extension
        robot.setExtensionPower(-gamepad2.left_stick_y);
//        // Pivot
//        if(gamepad2.left_bumper){
//            robot.setPivotPower(-1);
//        }else if(gamepad2.left_trigger>0.5){
//            robot.setPivotPower(0.5);
//        } else{
//            robot.setPivotPower(0);
//        }

        // Lift
        if(gamepad2.dpad_up){
            robot.setLiftPower(-1);
        } else if (gamepad2.dpad_down){
            robot.setLiftPower(1);
        }else{
            robot.setLiftPower(0);
        }

        // Hopper positions
        if(gamepad2.a){
            hopperPosition = HopperPosition.Idle;
        } else if(gamepad2.y){
            hopperPosition = HopperPosition.up;
        }

        switch(hopperPosition){
            case Idle:
                robot.setHopperPosition(1);
                break;
            case up:
                robot.setHopperPosition(0.1);
                break;
            default:
                robot.setHopperPosition(1);
                break;
        }

    }

    @Override
    public void stop() {
    }

}
