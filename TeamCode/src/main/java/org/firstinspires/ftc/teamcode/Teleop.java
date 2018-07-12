package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "CRITeleop", group = "Iterative Opmode")
//@Disabled
public class Teleop extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot = new Robot();
    private enum HopperPosition {Idle, level, up};
    private HopperPosition hopperPosition = HopperPosition.Idle;


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
        double xVelocity;
        double yVelocity;
        double wVelocity;

        yVelocity = gamepad1.left_stick_y*Math.abs(gamepad1.left_stick_y);
        xVelocity = gamepad1.left_stick_x*Math.abs(gamepad1.left_stick_x);
        wVelocity = gamepad1.right_stick_x*Math.abs(gamepad1.right_stick_x)*2/3;
        yVelocity = Range.clip(yVelocity, -1.0, 1.0);
        xVelocity = Range.clip(xVelocity, -1.0, 1.0);
        wVelocity = Range.clip(wVelocity, -1.0, 1.0);

        robot.drive(xVelocity, yVelocity, wVelocity);

        robot.runIntake(gamepad2.left_stick_y);
        robot.runLift(gamepad2.right_stick_y);

        if(gamepad2.a){
            hopperPosition = HopperPosition.Idle;
        } else if(gamepad2.x){
            hopperPosition = HopperPosition.level;
        } else if(gamepad2.y){
            hopperPosition = HopperPosition.up;
        }

        switch(hopperPosition){
            case Idle:
                robot.setHopperPosition(0.65);
                break;
            case level:
                robot.setHopperPosition(0.5);
                break;
            case up:
                robot.setHopperPosition(0);
                break;
            default:
                robot.setHopperPosition(0.5);
                break;
        }


        // Show the elapsed game time and velocities.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "X (%.2f), Y (%.2f), W (%.2f)", xVelocity, yVelocity, wVelocity);
    }

    @Override
    public void stop() {
    }

}
