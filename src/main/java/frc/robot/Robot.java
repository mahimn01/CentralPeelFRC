// package frc.robot;
// import edu.wpi.first.wpilibj.Timer;
// import com.ctre.phoenix.motorcontrol.*;
// import com.ctre.phoenix.motorcontrol.can.*;

// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.TimedRobot;

// public class Robot extends TimedRobot {
//     // Motor controllers
//     TalonSRX _talon0 = new TalonSRX(1);
//     TalonSRX _talon1 = new TalonSRX(3);
//     TalonSRX _talon2 = new TalonSRX(5);
//     TalonSRX _talon3 = new TalonSRX(2);
//     TalonSRX _talon4 = new TalonSRX(4);
//     TalonSRX _talon5 = new TalonSRX(6);

//     // Joystick (for teleop)
//     Joystick _joystick = new Joystick(0);

//     // Autonomous variables
//     private Timer autoTimer = new Timer();
//     private final double AUTO_TIME = 15.0; // Autonomous period in seconds
//     private final double TARGET_DISTANCE = 5.0; // Target distance in feet
//     private boolean reachedTarget = false;

//     @Override
//     public void robotInit() {
//         // Robot initialization code here (if any)
//     }

//     @Override
//     public void autonomousInit() {
//         // Reset and start the autonomous timer
//         autoTimer.reset();
//         autoTimer.start();
//         reachedTarget = false;

//         // Initial motor configuration for autonomous
//         // (Adjust as necessary for your robot's setup)
//         _talon0.setInverted(false);
//         _talon3.setInverted(true);
//         // Other motor initializations (if necessary)
//     }

//     @Override
//     public void autonomousPeriodic() {
//         // Check if the autonomous period is still active and the target hasn't been reached
//         if (autoTimer.get() < AUTO_TIME && !reachedTarget) {
//             // Example: drive straight at a certain speed
//             double speed = 0.5; // Adjust this value based on your robot

//             // Set motor speeds
//             _talon0.set(ControlMode.PercentOutput, speed);
//             _talon1.set(ControlMode.PercentOutput, speed);
//             _talon2.set(ControlMode.PercentOutput, speed);
//             _talon3.set(ControlMode.PercentOutput, speed);
//             _talon4.set(ControlMode.PercentOutput, speed);
//             _talon5.set(ControlMode.PercentOutput, speed);

//             // Limelight-based adjustments or distance checks can be added here
//             // Check if the robot has reached the target distance (5 feet)
//             // This is where you need to integrate your distance measuring logic

//             // If the target distance is reached, set reachedTarget to true
//             // reachedTarget = checkIfTargetReached(); // Implement this method or logic
//         } else {
//             // Stop the robot
//             stopMotors();
//         }
//     }

//     @Override
//     public void teleopInit() {
//         // Teleop initialization code here (if any)
//     }

//     @Override
//     public void teleopPeriodic() {
//         // Your existing teleop code
//         double speed = _joystick.getRawAxis(1) * 0.3;
//         double turn = _joystick.getRawAxis(4) * 0.3;

//         double left = speed + turn;
//         double right = speed - turn;

//         _talon0.set(ControlMode.PercentOutput, left);
//         _talon1.set(ControlMode.PercentOutput, left);
//         _talon2.set(ControlMode.PercentOutput, left);
//         _talon3.set(ControlMode.PercentOutput, -right);
//         _talon4.set(ControlMode.PercentOutput, -right);
//         _talon5.set(ControlMode.PercentOutput, -right);
//     }

//     // Utility method to stop all motors
//     private void stopMotors() {
//         _talon0.set(ControlMode.PercentOutput, 0);
//         _talon1.set(ControlMode.PercentOutput, 0);
//         _talon2.set(ControlMode.PercentOutput, 0);
//         _talon3.set(ControlMode.PercentOutput, 0);
//         _talon4.set(ControlMode.PercentOutput, 0);
//         _talon5.set(ControlMode.PercentOutput, 0);
//     }
// }




package frc.robot;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

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

    // Limelight variables
    private NetworkTable table;
    private NetworkTableEntry tx, ty, ta;
    private boolean m_LimelightHasValidTarget;
    private double m_LimelightDriveCommand;
    private double m_LimelightSteerCommand;

    // Distance tracking variables
    private boolean reachedTarget = false;
    private final double TARGET_DISTANCE = 2.0; // Target distance in feet

    @Override
    public void robotInit() {
        // Initialize NetworkTable for Limelight
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");

        // Initialize your distance sensor here (if any)
    }

    @Override
    public void autonomousInit() {
        // Autonomous initialization
        reachedTarget = false;
        // Reset your distance sensor here (if any)
    }

    @Override
    public void autonomousPeriodic() {
        // Autonomous mode logic (to be implemented)
    }

    @Override
    public void teleopInit() {
        // Teleop initialization
        reachedTarget = false;
        // Reset your distance sensor here (if any)
    }

    @Override
    public void teleopPeriodic() {
        if (!reachedTarget) {
            Update_Limelight_Tracking();
            reachedTarget = checkIfTargetReached(); // Checks if the target distance is reached
        } else {
            stopMotors();  // Stops the robot
        }
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

    // Limelight tracking method
    private void Update_Limelight_Tracking() {
        final double STEER_K = 0.03;  // Proportional steering constant
        final double DRIVE_K = 0.26;  // Proportional driving constant
        final double DESIRED_TARGET_AREA = 13.0;  // Area of the target at 2 feet
        final double MAX_DRIVE = 0.7;  // Maximum drive speed
2
        double tv = tx.getDouble(0.0);
        double txVal = tx.getDouble(0.0);
        double taVal = ta.getDouble(0.0);

        if (tv < 1.0) {
            m_LimelightHasValidTarget = false;
            m_LimelightDriveCommand = 0.0;
            m_LimelightSteerCommand = 0.0;
            return;
        }

        m_LimelightHasValidTarget = true;

        double steer_cmd = txVal * STEER_K;
        m_LimelightSteerCommand = steer_cmd;

        double drive_cmd = (DESIRED_TARGET_AREA - taVal) * DRIVE_K;
        if (drive_cmd > MAX_DRIVE) {
            drive_cmd = MAX_DRIVE;
        }
        m_LimelightDriveCommand = drive_cmd;

        // Apply the calculated values to the motors
        _talon0.set(ControlMode.PercentOutput, m_LimelightDriveCommand + m_LimelightSteerCommand);
        _talon1.set(ControlMode.PercentOutput, m_LimelightDriveCommand + m_LimelightSteerCommand);
        _talon2.set(ControlMode.PercentOutput, m_LimelightDriveCommand + m_LimelightSteerCommand);
        _talon3.set(ControlMode.PercentOutput, -m_LimelightDriveCommand + m_LimelightSteerCommand);
        _talon4.set(ControlMode.PercentOutput, -m_LimelightDriveCommand + m_LimelightSteerCommand);
        _talon5.set(ControlMode.PercentOutput, -m_LimelightDriveCommand + m_LimelightSteerCommand);
    }

    // Method to check if the target distance is reached
    private boolean checkIfTargetReached() {
        // Implement the logic to check if the robot has moved 2 feet
        // Assuming you have a method to get the distance traveled
        // return getDistanceTraveled() >= TARGET_DISTANCE;
        return false; // Replace this with actual distance checking
    }

    // Example method to get the distance traveled (using an encoder, for instance)
    private double getDistanceTraveled() {
    
        return 0; // Replace this with actual distance measurement
    }
}

