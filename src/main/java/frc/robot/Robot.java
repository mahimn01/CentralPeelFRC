package frc.robot;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

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

    @Override
    public void robotInit() {
        // Robot initialization code here (if any)
    }

    @Override
    public void autonomousInit() {
        // Reset and start the autonomous timer
        autoTimer.reset();
        autoTimer.start();
        reachedTarget = false;

        // Initial motor configuration for autonomous
        // (Adjust as necessary for your robot's setup)
        _talon0.setInverted(false);
        _talon3.setInverted(true);
        // Other motor initializations (if necessary)
    }

    @Override
    public void autonomousPeriodic() {
        // Check if the autonomous period is still active and the target hasn't been reached
        if (autoTimer.get() < AUTO_TIME && !reachedTarget) {
            // Example: drive straight at a certain speed
            double speed = 0.5; // Adjust this value based on your robot

            // Set motor speeds
            _talon0.set(ControlMode.PercentOutput, speed);
            _talon1.set(ControlMode.PercentOutput, speed);
            _talon2.set(ControlMode.PercentOutput, speed);
            _talon3.set(ControlMode.PercentOutput, speed);
            _talon4.set(ControlMode.PercentOutput, speed);
            _talon5.set(ControlMode.PercentOutput, speed);

            // Limelight-based adjustments or distance checks can be added here
            // Check if the robot has reached the target distance (5 feet)
            // This is where you need to integrate your distance measuring logic

            // If the target distance is reached, set reachedTarget to true
            // reachedTarget = checkIfTargetReached(); // Implement this method or logic
        } else {
            // Stop the robot
            stopMotors();
        }
    }

    @Override
    public void teleopInit() {
        // Teleop initialization code here (if any)
    }

    @Override
    public void teleopPeriodic() {
        // Your existing teleop code
        double speed = _joystick.getRawAxis(1) * 0.3;
        double turn = _joystick.getRawAxis(4) * 0.3;

        double left = speed + turn;
        double right = speed - turn;

        _talon0.set(ControlMode.PercentOutput, left);
        _talon1.set(ControlMode.PercentOutput, left);
        _talon2.set(ControlMode.PercentOutput, left);
        _talon3.set(ControlMode.PercentOutput, -right);
        _talon4.set(ControlMode.PercentOutput, -right);
        _talon5.set(ControlMode.PercentOutput, -right);
    }

    // Utility method to stop all motors
    private void stopMotors() {
        _talon0.set(ControlMode.PercentOutput, 0);
        _talon1.set(ControlMode.PercentOutput, 0);
        _talon2.set(ControlMode.PercentOutput, 0);
        _talon3.set(ControlMode.PercentOutput, 0);
        _talon4.set(ControlMode.PercentOutput, 0);
        _talon5.set(ControlMode.PercentOutput, 0);
    }
}
