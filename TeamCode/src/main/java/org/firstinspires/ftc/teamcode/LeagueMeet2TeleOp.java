package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DropCommand;
import org.firstinspires.ftc.teamcode.commands.ResetCommand;
import org.firstinspires.ftc.teamcode.commands.drive.DefaultDriveCommand;
import org.firstinspires.ftc.teamcode.commands.drive.SlowDriveCommand;
import org.firstinspires.ftc.teamcode.drive.MatchOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

import org.firstinspires.ftc.teamcode.subsystems.ArmServos;
import org.firstinspires.ftc.teamcode.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@Config
@TeleOp(name = "League Meet 2 TeleOp")
public class LeagueMeet2TeleOp extends MatchOpMode {
    // Motors
    private MotorEx leftFront,  leftRear, rightRear,  rightFront;
    private MotorEx liftMotor;
    private MotorEx intakeMotor;
    private MotorEx carouselMotor;
    private ServoEx armServo, dropServo;

    // Gamepad
    private GamepadEx driverGamepad, operatorGamepad;

    // Subsystems
    private Drivetrain drivetrain;
    private Lift lift;
    private Intake intake;
    private ArmServos armServos;
    private Carousel carousel;

    //Buttons
    private Button intakeButton, halfSpeedButton, outtakeButton;
    private Button slowModeTrigger;
    public Button liftUpButton, liftDownButton;
    public Button liftLowButton, liftMidButton, liftHighButton;
    public Button armServoHomeButton, armServoDropButton, armServoMidButton;
    public Button carouselRightButton, carouselLeftButton;
    public Button resetArmButton;
    public Button dropBoxButton;
    public Button restBoxButton;

    @Override
    public void robotInit() {

        // Intake hardware Initializations
        intakeMotor = new MotorEx(hardwareMap, "intake");
        // Lift hardware initializations
        liftMotor = new MotorEx(hardwareMap, "lift");
        //carouse hardware initializations
        carouselMotor = new MotorEx(hardwareMap, "carousel");
        // Servos hardware initializations
        armServo = new SimpleServo(hardwareMap,"arm", 0, 360);
        dropServo = new SimpleServo(hardwareMap, "drop",0,360);

        // Subsystems
        drivetrain = new Drivetrain(new SampleTankDrive(hardwareMap),telemetry);
        drivetrain.init();
        intake = new Intake(intakeMotor, telemetry);
        lift = new Lift(liftMotor, telemetry);
        armServos = new ArmServos(armServo, dropServo, telemetry);
        carousel = new Carousel(carouselMotor, telemetry);

        //gamepad1.setJoystickDeadzone(0.0f);
        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);
        drivetrain.setDefaultCommand(new DefaultDriveCommand(drivetrain, driverGamepad));

    }

    @Override
    public void configureButtons() {

        //slowmode
        slowModeTrigger = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER)).whileHeld(new SlowDriveCommand(drivetrain, driverGamepad));

        //intake
        outtakeButton = (new GamepadTrigger(driverGamepad, GamepadKeys.Trigger.LEFT_TRIGGER).whileHeld(intake::outtake).whenReleased(intake::stop));
        intakeButton = (new GamepadTrigger(driverGamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).whileHeld(intake::intake).whenReleased(intake::stop));


        //lift
        //Don't use any other D-Pad except down
        liftUpButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_UP).whenPressed(lift::liftManual).whenReleased(lift::stopLift));
        liftDownButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_LEFT).whenPressed(lift::lowerLiftManual).whenReleased(lift::stopLift));

        //reset motion
        resetArmButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_DOWN)).whenPressed(
                new ResetCommand(armServos, lift)
        );

        liftLowButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.X).whenPressed(lift::liftLow));
        liftMidButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.B).whenPressed(lift::liftMid));
        liftHighButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.Y).whenPressed(lift::liftHigh));

        //carousel
        carouselLeftButton = (new GamepadTrigger(operatorGamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).whenPressed(carousel::carouselLeft).whenReleased(carousel::stop));
        carouselRightButton = (new GamepadTrigger(operatorGamepad, GamepadKeys.Trigger.LEFT_TRIGGER).whenPressed(carousel::carouselRight).whenReleased(carousel::stop));

        //Arm up
        armServoMidButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.A).whenPressed(armServos::armUp));

        //drop motion
        dropBoxButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.RIGHT_BUMPER)).whenPressed(
                new DropCommand(armServos)
        );

        //rest position for box
        restBoxButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.LEFT_BUMPER)).whenPressed(armServos::armHome);

        /*
        lift low position: X
        lift mid position: B
        lift high position: Y
        lift reset position: DPAD_DOWN

        box position up: A
        box position outtake: RIGHT_BUMPER
         */


/*
        singleFeedButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.Y)).whenPressed(new FeedRingsCommand(feeder, 1));
        // TRIPLE SHOT SPEED *********************
        tripleFeedButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_BUMPER)).whenPressed(new SmartFeedRingsCommand(feeder, shooterWheels,4,30));
        shootButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER)).toggleWhenPressed(
                new InstantCommand(() -> shooterWheels.setShooterRPM(ShooterWheels.TARGET_SPEED), shooterWheels),
                new InstantCommand(() -> shooterWheels.setShooterRPM(0), shooterWheels));
        (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_STICK_BUTTON)).whileHeld(new VisionCommand(drivetrain, vision, 30));
        powershotButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.B)).toggleWhenPressed(
                new InstantCommand(() -> shooterWheels.setShooterRPM(2900), shooterWheels),
                new InstantCommand(() -> shooterWheels.setShooterRPM(0), shooterWheels));
        intakeButton = (new GamepadTrigger(driverGamepad, GamepadKeys.Trigger.LEFT_TRIGGER)).whileHeld(intake::intake).whenReleased(intake::stop);
        outtakeButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.X)).whileHeld(intake::outtake).whenReleased(intake::stop);

        toggleClawButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.A)).toggleWhenPressed(
                new InstantCommand(wobbleGoalArm::openClaw, wobbleGoalArm),
                new InstantCommand(wobbleGoalArm::closeClaw, wobbleGoalArm)
        );

        liftArmButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP)).whenPressed(wobbleGoalArm::liftWobbleGoal);
        lowerArmButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN)).whenPressed(wobbleGoalArm::placeWobbleGoal);


        (new GamepadButton(driverGamepad, GamepadKeys.Button.BACK)).whenPressed(() -> shooterWheels.adjustShooterRPM(75));
        (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT)).whenPressed(() -> shooterWheels.adjustShooterRPM(-75));
        lowMidWobbleButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_RIGHT)).whenPressed(() -> wobbleGoalArm.setWobbleGoal(-65));
        (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_STICK_BUTTON)).toggleWhenPressed(new InstantCommand(intake::dropIntake, intake), new InstantCommand(intake::liftIntake, intake));
        drivetrain.setDefaultCommand(new DefaultDriveCommand(drivetrain, driverGamepad));
        (new GamepadButton(operatorGamepad, GamepadKeys.Button.Y)).whenPressed(wobbleGoalArm::liftArmManual).whenReleased(wobbleGoalArm::stopArm);
        (new GamepadButton(operatorGamepad, GamepadKeys.Button.X)).whenPressed(wobbleGoalArm::lowerArmManual).whenReleased(wobbleGoalArm::stopArm);
 */
        //drivetrain.setDefaultCommand(new DefaultDriveCommand(drivetrain, driverGamepad));
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void matchStart() {
    }

    @Override
    public void robotPeriodic() {
    }
}