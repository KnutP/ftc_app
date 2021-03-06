package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import static java.lang.Thread.sleep;


class MecanumDrivetrain extends AbstractDrivetrain{

    private static final double WIDTH = 16;
    private static final double LENGTH = 5;
    private static final double MAX_SPEED = 1.0;

    private Robot robot;
//    private ElapsedTime gyroTimer = new ElapsedTime();

    private double lastTime = 0;
    private double lastError = 0;
    private double speed = 0;

    public MecanumDrivetrain(Robot r) {
        robot = r;
    }

    // Moves the drive train using the given x, y, and rotational velocities
    @Override
    public void drive(double xVelocity, double yVelocity, double wVelocity) {
        double speedScale = 1.0;

        double rfVelocity = yVelocity - xVelocity + wVelocity * (WIDTH / 2 + LENGTH / 2);
        double lfVelocity = yVelocity + xVelocity - wVelocity * (WIDTH / 2 + LENGTH / 2);
        double lbVelocity = yVelocity - xVelocity - wVelocity * (WIDTH / 2 + LENGTH / 2);
        double rbVelocity = yVelocity + xVelocity + wVelocity * (WIDTH / 2 + LENGTH / 2);

        double maxVelocity = findMax(lfVelocity, rfVelocity, lbVelocity, rbVelocity);

        if (maxVelocity > MAX_SPEED) {
            speedScale = MAX_SPEED / maxVelocity;
        }

        lfDriveM.setPower(lfVelocity * speedScale);
        rfDriveM.setPower(rfVelocity * speedScale);
        lbDriveM.setPower(lbVelocity * speedScale);
        rbDriveM.setPower(rbVelocity * speedScale);

    }

    @Override
    public void encoderDrive(double yDist, double maxSpeed) {
        LinearOpMode linOp = (LinearOpMode)robot.opMode;
        int ENCODER_CPR = 1120; // Encoder counts per Revolution
        double gearRatio = 1.5; // [Gear Ratio]:1
        double circumference = 12.5; // Wheel circumference (in inches)
        double distanceToDrive = yDist / (circumference * gearRatio); // Number of rotations to drive
        double COUNTS = ENCODER_CPR * distanceToDrive; // Number of encoder counts to drive

        int rTarget = (rfDriveM.getCurrentPosition() + (int) COUNTS);
        int lTarget =(lfDriveM.getCurrentPosition() + (int) COUNTS);

        if (yDist > 0) {
            while (rfDriveM.getCurrentPosition() < rTarget - 5 &&
                    lfDriveM.getCurrentPosition() < lTarget - 5 && linOp.opModeIsActive()){
                drive(0, -maxSpeed, 0);
                robot.opMode.telemetry.addData("1. rPosition", rfDriveM.getCurrentPosition());
                robot.opMode.telemetry.addData("2. rTarget", rTarget);
                robot.opMode.updateTelemetry(robot.opMode.telemetry);
            }
            super.stop();
        } else if (yDist < 0) {
            while (rfDriveM.getCurrentPosition() > rTarget + 5 &&
                    lfDriveM.getCurrentPosition() > lTarget + 5 && linOp.opModeIsActive()) {
                drive(0, maxSpeed, 0);
                robot.opMode.telemetry.addData("1. rpPosition", rfDriveM.getCurrentPosition());
                robot.opMode.telemetry.addData("2. rTarget", rTarget);
                robot.opMode.updateTelemetry(robot.opMode.telemetry);
            }
            super.stop();
        }
    }

//    @Override
//    public void gyroTurn(int degreesToTurn, double maxSpeed){
//        updateIMU();
//        /* For us, the IMU has had us turn just a bit more than what we intend. The operation
//         * below accounts for this by dividing the current degreesToTurn value by 8/9.
//         */
//        degreesToTurn = (degreesToTurn * 8) / 9;
//
//        // Now we define our variables
//        int targetHeading = Math.abs(degreesToTurn) - (int)angles.firstAngle;
//
//        /* These operations account for when the robot would cross the IMU rotation line, which
//         * separates -180 from 180. Adding or subtracting the degreesToTurn by 360 here isn't
//         * always necessary, however, so we skip this operation in those cases */
//        targetHeading += targetHeading > 180 ? -360 :
//                targetHeading < -180 ? 360 : 0;
//
//        /* In case you don't know what this is (it isn't widely used in FTC Robot programs, I
//         * don't think), it's called an Array, and they're used to store a list of variables in
//         * Java programs.
//         * This array stores 5 heading variables that are very close or equal to our target heading.
//         * We will use them to determine if we are in range of where we want Legacy to turn
//         */
//        int[] headingList = {targetHeading - 2,
//                targetHeading - 1,
//                targetHeading,
//                targetHeading + 1,
//                targetHeading + 2};
//
//        /* As you can probably see here, the values in an array are mutable, which works very well
//         * in cases like this, where are target heading values might be above or below where our
//         * IMU can read.
//         */
//        for(int i = 0; i < headingList.length; i++) {
//            headingList[i] += headingList[i] < -180 ? 360 :
//                    headingList[i] > 180 ? -360 : 0;
//        }
//
//        // Finally, we reset the Gyro timer and begin our turn!
////        gyroTimer.reset();
//
//        // RIGHT TURN
//        if (degreesToTurn > 0) {
//            while (shouldKeepTurning2(headingList)) {
//                updateIMU();
//                drive(0, 0, 0.35);
//                robot.opMode.telemetry.addData("Gyro", -angles.firstAngle);
//                robot.opMode.telemetry.addData("Target", targetHeading);
//                robot.opMode.telemetry.update();
//                if(((LinearOpMode)robot.opMode).isStopRequested()) {             // Break if we hit stop
//                    break;
//                }
//            }
//            // LEFT TURN
//        } else {
//            while (shouldKeepTurning2(headingList)) {
//                updateIMU();
//                drive(0, 0, -0.35);
//                robot.opMode.telemetry.addData("Gyro", -angles.firstAngle);
//                robot.opMode.telemetry.addData("Target", targetHeading);
//                robot.opMode.telemetry.update();
//                if(((LinearOpMode)robot.opMode).isStopRequested()) {             // Break if we hit stop
//                    break;
//                }
//            }
//        }
//        robot.drivetrain.stop();
//    }

    public void gyroTurn(int degreesToTurn, String direction) {
        updateIMU();

        /* For us, the IMU has had us turn just a bit more than what we intend. The operation
         * below accounts for this by dividing the current degreesToTurn value by 8/9.
         */
        degreesToTurn = (degreesToTurn * 8) / 9;

        // Now we define our variables
        int targetHeading = degreesToTurn - (int)angles.firstAngle;

        /* These operations account for when the robot would cross the IMU rotation line, which
         * separates -180 from 180. Adding or subtracting the degreesToTurn by 360 here isn't
         * always necessary, however, so we skip this operation in those cases */
        targetHeading += targetHeading > 180 ? -360 :
                targetHeading < -180 ? 360 : 0;

        /* In case you don't know what this is (it isn't widely used in FTC Robot programs, I
         * don't think), it's called an Array, and they're used to store a list of variables in
         * Java programs.
         * This array stores 5 heading variables that are very close or equal to our target heading.
         * We will use them to determine if we are in range of where we want Legacy to turn
         */
        int[] headingList = {targetHeading - 2,
                targetHeading - 1,
                targetHeading,
                targetHeading + 1,
                targetHeading + 2};

        /* As you can probably see here, the values in an array are mutable, which works very well
         * in cases like this, where are target heading values might be above or below where our
         * IMU can read.
         */
        for(int i = 0; i < headingList.length; i++) {
            headingList[i] += headingList[i] < -180 ? 360 :
                    headingList[i] > 180 ? -360 : 0;
        }

        // RIGHT TURN
        if (direction == "RIGHT") {
            while (shouldKeepTurning2(headingList)) {
                updateIMU();
                drive(0, 0, 0.35);
                robot.opMode.telemetry.addData("Gyro", -angles.firstAngle);
                robot.opMode.telemetry.addData("Target", targetHeading);
                robot.opMode.telemetry.update();
                if (((LinearOpMode) robot.opMode).isStopRequested()) {             // Break if we hit stop
                    break;
                }
            }
            // LEFT TURN
        } else {
            while (shouldKeepTurning2(headingList)) {
                updateIMU();
                drive(0, 0, -0.35);
                robot.opMode.telemetry.addData("Gyro", -angles.firstAngle);
                robot.opMode.telemetry.addData("Target", targetHeading);
                robot.opMode.telemetry.update();
                if(((LinearOpMode)robot.opMode).isStopRequested()) {             // Break if we hit stop
                    break;
                }
            }
        }
        robot.drivetrain.stop();
    }

    public boolean shouldKeepTurning2(int[] listOfHeadings) {
        for(int heading : listOfHeadings) {
            if(heading == (int)-angles.firstAngle) {
                return false;
            }
        }

        return true;
    }

    private void updateIMU() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    // Helper function for power scaling in the drive method
    private double findMax(double... vals) {
        double max = Double.NEGATIVE_INFINITY;

        for (double d : vals) {
            if (d > max) max = d;
        }

        return max;
    }
}