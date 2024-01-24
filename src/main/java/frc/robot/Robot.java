package frc.robot;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.subsystems.LimeLight;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    // Motor controllers
    TalonSRX _talon0 = new TalonSRX(1);
    TalonSRX _talon1 = new TalonSRX(3);
    TalonSRX _talon2 = new TalonSRX(5);
    TalonSRX _talon3 = new TalonSRX(2);
    TalonSRX _talon4 = new TalonSRX(4);
    TalonSRX _talon5 = new TalonSRX(6);

    // Joystick (for teleop)
    Joystick _joystick = new Joystick(0);

    // Autonomous variables
    private Timer autoTimer = new Timer();
    private final double AUTO_TIME = 15.0; // Autonomous period in seconds
    private final double TARGET_DISTANCE = 5.0; // Target distance in feet
    private boolean reachedTarget = false;

    private final LimeLight m_limelight = new LimeLight();

    @Override
    public void robotInit() {
        // Initialize Network Tables
        // limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

        // Robot initialization code here (if any)
    }

    @Override
    public void autonomousInit() {
        // Reset and start the autonomous timer
        autoTimer.reset();
        autoTimer.start();
        reachedTarget = false;

        // Initial motor configuration for autonomous
        _talon0.setInverted(false);
        _talon2.setInverted(false);
        _talon4.setInverted(false);
        _talon1.setInverted(true);
        _talon3.setInverted(true);
        _talon5.setInverted(true);
    }

    @Override
    public void autonomousPeriodic() {
        m_limelight.periodic();
        // // Check if the autonomous period is still active and the target hasn't been reached
        // // if (autoTimer.get() < AUTO_TIME && !reachedTarget) {
        //     // Get Limelight data
        //     NetworkTableEntry tv = limelightTable.getEntry("tv"); // Whether the limelight has any valid targets (0 or 1)
        //     NetworkTableEntry tx = limelightTable.getEntry("tx"); // Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
        //     NetworkTableEntry ty = limelightTable.getEntry("ty"); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
        //     double targetVisible = tv.getDouble(1);
        //     double targetX = tx.getDouble(1);
        //     double targetY = ty.getDouble(0);
        //     SmartDashboard.putNumber("TX", targetX);
        //     SmartDashboard.putNumber("TV", targetVisible);
        //     // if (targetVisible) {
             
        //         double steer = targetX * 0.3;
        //         double drive = -0.02 * targetY;
        //         drive = Math.max(drive, 0.4);

        //         double leftCommand = drive + steer;
        //         double rightCommand = drive - steer;

        //         _talon0.set(ControlMode.PercentOutput, leftCommand);
        //         _talon1.set(ControlMode.PercentOutput, leftCommand);
        //         _talon2.set(ControlMode.PercentOutput, leftCommand);
        //         _talon3.set(ControlMode.PercentOutput, rightCommand);
        //         _talon4.set(ControlMode.PercentOutput, rightCommand);
        //         _talon5.set(ControlMode.PercentOutput, rightCommand);
        //     // } else {
        //         // Stop or search for target
        //         // stopMotors();
        //     // }

        // // } else {
        //     // Stop the robot
        //     // stopMotors();
        // // }
    }

    void stopMotors() {
      _talon0.set(ControlMode.PercentOutput, 0);
      _talon1.set(ControlMode.PercentOutput, 0);
      _talon2.set(ControlMode.PercentOutput, 0);
      _talon3.set(ControlMode.PercentOutput, 0);
      _talon4.set(ControlMode.PercentOutput, 0);
      _talon5.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void teleopPeriodic() {
    }
  }
