package org.firstinspires.ftc.teamcode.autons.lmchamp.red.Warehouse;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.AutoIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.CapArmCommands.CapArmLowCommand;
import org.firstinspires.ftc.teamcode.commands.DriveCommands.SplineCommand;
import org.firstinspires.ftc.teamcode.commands.DriveCommands.TurnToCommand;
import org.firstinspires.ftc.teamcode.commands.DropFreightCommands.AutoDropFreightCommand;
import org.firstinspires.ftc.teamcode.commands.LiftCommands.LiftHighCommand;
import org.firstinspires.ftc.teamcode.commands.LiftCommands.AutoLiftResetCommand;
import org.firstinspires.ftc.teamcode.subsystems.ArmServos;
import org.firstinspires.ftc.teamcode.subsystems.CapServos;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.SensorColor;

public class CRedWarehouseCommandL extends SequentialCommandGroup {
    public CRedWarehouseCommandL(Drivetrain drivetrain, Intake intake, Lift lift, ArmServos armServos, SensorColor sensorColor, CapServos capServos) {
        //declare variables here
        //Low
        addCommands(

                new InstantCommand(intake::intake),
                new SplineCommand(drivetrain, new Vector2d(-8,-30), Math.toRadians(272)),

                new AutoIntakeCommand(lift, intake, armServos, drivetrain, sensorColor),
                new LiftHighCommand(lift, armServos),
                //new TurnToCommand(drivetrain, 280),
                new SplineCommand(drivetrain, new Vector2d(19.7,19.2), Math.toRadians(0), true),
                new AutoDropFreightCommand(armServos, drivetrain),

                new AutoLiftResetCommand(armServos, lift),
                new InstantCommand(intake::intake),
                new SplineCommand(drivetrain, new Vector2d(-8,-33), Math.toRadians(272)),

                new AutoIntakeCommand(lift, intake, armServos, drivetrain, sensorColor),
                new LiftHighCommand(lift, armServos),
                //new TurnToCommand(drivetrain, 280),
                new SplineCommand(drivetrain, new Vector2d(19.7,17.45), Math.toRadians(0), true),
                new AutoDropFreightCommand(armServos, drivetrain),

                new AutoLiftResetCommand(armServos, lift),
                new SplineCommand(drivetrain, new Vector2d(-8,-37), Math.toRadians(272)),

                new AutoIntakeCommand(lift, intake, armServos, drivetrain, sensorColor),
                new LiftHighCommand(lift, armServos),
                //new TurnToCommand(drivetrain, 280),
                new SplineCommand(drivetrain, new Vector2d(19.7,15.8), Math.toRadians(0), true),
                new AutoDropFreightCommand(armServos, drivetrain),

                new AutoLiftResetCommand(armServos, lift),
                new SplineCommand(drivetrain, new Vector2d(-8,-40), Math.toRadians(270))
        );
    }
}