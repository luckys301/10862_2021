package org.firstinspires.ftc.teamcode.autons.lm2.blue.Warehouse;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SelectCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Util;
import org.firstinspires.ftc.teamcode.drive.MatchOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.pipelines.TeamMarkerPipeline;
import org.firstinspires.ftc.teamcode.subsystems.ArmServos;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

import java.util.HashMap;
import java.util.logging.Level;

@Autonomous(name = "Blue Warehouse", group = "BLUE")
public class BlueWarehouseAuton extends MatchOpMode {
public static double startPoseX = 0;
public static double startPoseY = 0;
public static double startPoseHeading = 0;

// Motors
private MotorEx leftFront, leftRear, rightRear, rightFront;
private MotorEx intakeMotor;
private MotorEx liftMotor;
private ServoEx dropServo, armServo;

// Gamepad
private GamepadEx driverGamepad;

// Subsystems
private Drivetrain drivetrain;
private Intake intake;
private Lift lift;
private Vision vision;
private ArmServos armServos;

@Override
public void robotInit() {
    // Subsystems
    drivetrain = new Drivetrain(new SampleTankDrive(hardwareMap), telemetry);
    drivetrain.init();
    intakeMotor = new MotorEx(hardwareMap, "intake");
    liftMotor = new MotorEx(hardwareMap, "lift", Motor.GoBILDA.RPM_117);
    //TODO: Do I need to change the RPM? - won't make a difference

    //drivetrain.setPoseEstimate(Trajectories.BlueLeftTape.startPose);
    vision = new Vision(hardwareMap, "Webcam 1", telemetry);
    drivetrain.setPoseEstimate(new Pose2d(startPoseX, startPoseY, Math.toRadians(startPoseHeading)));
    intake = new Intake(intakeMotor, telemetry);
    lift = new Lift(liftMotor, telemetry);
    armServos = new ArmServos(armServo, dropServo, telemetry);}

@Override
public void disabledPeriodic() {
    Util.logger(this, telemetry, Level.INFO, "Current Position", vision.getCurrentPosition());
    vision.periodic();
}

@Override
public void matchStart() {
    schedule(
            new SelectCommand(new HashMap<Object, Command>() {{
                put(TeamMarkerPipeline.Position.LEFT, new SequentialCommandGroup(
                        new BlueWarehouseCommandL(drivetrain, intake, lift, armServos)
                ));
                put(TeamMarkerPipeline.Position.MIDDLE, new SequentialCommandGroup(
                        new BlueWarehouseCommandC(drivetrain, intake, lift, armServos)
                ));
                put(TeamMarkerPipeline.Position.RIGHT, new SequentialCommandGroup(
                        new BlueWarehouseCommandR(drivetrain, intake, lift, armServos)
                ));
            }}, vision::getCurrentPosition)
    );

}
}