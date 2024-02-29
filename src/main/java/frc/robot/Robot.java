package frc.robot;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.cameraserver.CameraServer;

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

    // Network Tables for Limelight
    NetworkTable limelightTable;

    @Override
    public void robotInit() {
        // Initialize Network Tables
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        

        // Robot initialization code here (if any)
        CameraServer.startAutomaticCapture();
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
        // Check if the autonomous period is still active and the target hasn't been reached
        // if (autoTimer.get() < AUTO_TIME && !reachedTarget) {
            // Get Limelight data
            NetworkTableEntry tv = limelightTable.getEntry("tv"); // Whether the limelight has any valid targets (0 or 1)
            NetworkTableEntry tx = limelightTable.getEntry("tx"); // Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
            NetworkTableEntry ty = limelightTable.getEntry("ty"); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
            double targetVisible = tv.getDouble(0);
            double targetX = tx.getDouble(0);
            double targetY = ty.getDouble(0);
            SmartDashboard.putNumber("TX", targetX);
            SmartDashboard.putNumber("TV", targetVisible);
            // if (targetVisible) {
             
                double steer = targetX * 0.3;
                double drive = 0.1 * targetY;
                drive = Math.max(drive, 0.6);

                double leftCommand = drive + steer;
                double rightCommand = drive - steer;

                _talon0.set(ControlMode.PercentOutput, leftCommand);
                _talon1.set(ControlMode.PercentOutput, leftCommand);
                _talon2.set(ControlMode.PercentOutput, leftCommand);
                _talon3.set(ControlMode.PercentOutput, rightCommand);
                _talon4.set(ControlMode.PercentOutput, rightCommand);
                _talon5.set(ControlMode.PercentOutput, rightCommand);
            // } else {
                // Stop or search for target
                // stopMotors();
            // }

        // } else {
            // Stop the robot
            // stopMotors();
        // }
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
    public void teleopInit() {
        _talon0.setInverted(false); // pick CW versus CCW when motor controller is positive/green
    }

    @Override
    public void teleopPeriodic() {
      // double stick = _joystick.getRawAxis(1) * -1; // make forward stick positive
      // System.out.println("stick:" + stick);
      // double speed = _joystick.getRawAxis(1) * -0.1;
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
}











// // package frc.robot;
// // import edu.wpi.first.wpilibj.Timer;
// // import com.ctre.phoenix.motorcontrol.*;
// // import com.ctre.phoenix.motorcontrol.can.*;
// // import edu.wpi.first.wpilibj.Joystick;
// // import edu.wpi.first.wpilibj.TimedRobot;
// // import edu.wpi.first.networktables.NetworkTable;
// // import edu.wpi.first.networktables.NetworkTableEntry;
// // import edu.wpi.first.networktables.NetworkTableInstance;

// // import frc.robot.subsystems.LimeLight;

// // import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// // public class Robot extends TimedRobot {
// //     // Motor controllers
// //     TalonSRX _talon0 = new TalonSRX(1);
// //     TalonSRX _talon1 = new TalonSRX(3);
// //     TalonSRX _talon2 = new TalonSRX(5);
// //     TalonSRX _talon3 = new TalonSRX(2);
// //     TalonSRX _talon4 = new TalonSRX(4);
// //     TalonSRX _talon5 = new TalonSRX(6);

// //     // Joystick (for teleop)
// //     Joystick _joystick = new Joystick(0);

// //     // Autonomous variables
// //     private Timer autoTimer = new Timer();
// //     private final double AUTO_TIME = 15.0; // Autonomous period in seconds
// //     private final double TARGET_DISTANCE = 5.0; // Target distance in feet
// //     private boolean reachedTarget = false;

// //     private final LimeLight m_limelight = new LimeLight();

// //     @Override
// //     public void robotInit() {
// //         // Initialize Network Tables
// //         // limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

// //         // Robot initialization code here (if any)
// //     }

// //     @Override
// //     public void autonomousInit() {
// //         // Reset and start the autonomous timer
// //         autoTimer.reset();
// //         autoTimer.start();
// //         reachedTarget = false;

// //         // Initial motor configuration for autonomous
// //         _talon0.setInverted(false);
// //         _talon2.setInverted(false);
// //         _talon4.setInverted(false);
// //         _talon1.setInverted(true);
// //         _talon3.setInverted(true);
// //         _talon5.setInverted(true);
// //     }

// //     @Override
// //     public void autonomousPeriodic() {
// //         m_limelight.periodic();
// //         // // Check if the autonomous period is still active and the target hasn't been reached
// //         // // if (autoTimer.get() < AUTO_TIME && !reachedTarget) {
// //         //     // Get Limelight data
// //         //     NetworkTableEntry tv = limelightTable.getEntry("tv"); // Whether the limelight has any valid targets (0 or 1)
// //         //     NetworkTableEntry tx = limelightTable.getEntry("tx"); // Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
// //         //     NetworkTableEntry ty = limelightTable.getEntry("ty"); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
// //         //     double targetVisible = tv.getDouble(1);
// //         //     double targetX = tx.getDouble(1);
// //         //     double targetY = ty.getDouble(0);
// //         //     SmartDashboard.putNumber("TX", targetX);
// //         //     SmartDashboard.putNumber("TV", targetVisible);
// //         //     // if (targetVisible) {
             
// //         //         double steer = targetX * 0.3;
// //         //         double drive = -0.02 * targetY;
// //         //         drive = Math.max(drive, 0.4);

// //         //         double leftCommand = drive + steer;
// //         //         double rightCommand = drive - steer;

// //         //         _talon0.set(ControlMode.PercentOutput, leftCommand);
// //         //         _talon1.set(ControlMode.PercentOutput, leftCommand);
// //         //         _talon2.set(ControlMode.PercentOutput, leftCommand);
// //         //         _talon3.set(ControlMode.PercentOutput, rightCommand);
// //         //         _talon4.set(ControlMode.PercentOutput, rightCommand);
// //         //         _talon5.set(ControlMode.PercentOutput, rightCommand);
// //         //     // } else {
// //         //         // Stop or search for target
// //         //         // stopMotors();
// //         //     // }

// //         // // } else {
// //         //     // Stop the robot
// //         //     // stopMotors();
// //         // // }
// //     }

// //     void stopMotors() {
// //       _talon0.set(ControlMode.PercentOutput, 0);
// //       _talon1.set(ControlMode.PercentOutput, 0);
// //       _talon2.set(ControlMode.PercentOutput, 0);
// //       _talon3.set(ControlMode.PercentOutput, 0);
// //       _talon4.set(ControlMode.PercentOutput, 0);
// //       _talon5.set(ControlMode.PercentOutput, 0);
// //     }

// //     @Override
// //     public void teleopPeriodic() {
// //     }
// //   }

// package frc.robot;
// import edu.wpi.first.wpilibj.Timer;
// import com.ctre.phoenix.motorcontrol.*;
// import com.ctre.phoenix.motorcontrol.can.*;
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;
// // import edu.wpi.first.cameraserver.CameraServer;
// // import edu.wpi.first.networktables.NetworkTable;
// // import edu.wpi.first.networktables.NetworkTableEntry;
// // import edu.wpi.first.networktables.NetworkTableInstance;
// // import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// public class Robot {

// private static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
// //x location of the target
// private static NetworkTableEntry tx = table.getEntry("tx");
// //y location of the target
// private static NetworkTableEntry ty = table.getEntry("ty");
// //area of the target
// private static NetworkTableEntry ta = table.getEntry("ta");
// //does the limelight have a target
// private static NetworkTableEntry tv = table.getEntry("tv");

// public static double getX(){
// 	return tx.getDouble(0.0);
// }

// public static double getY(){
// 	return ty.getDouble(0.0);
// }

// public static double getDistance(){
// 	double heightOfCamera = 43;
// 	double heightOfTarget = 29;
// 	double angleOfCamera = -20;
// 	double angleofTarget =  ty.getDouble(0.0);
// 	return (heightOfTarget - heightOfCamera) / Math.tan(Math.toRadians(angleOfCamera + angleofTarget));
// }

// public static double getArea(){
// 	return ta.getDouble(0.0);
// }

// public static boolean isTargetAvalible(){
// 	return tv.getBoolean(false);
// }
// //Moding the limelight to work for the individual case
// public static void drivingMode(boolean driveMode){
// 	ledMode(!driveMode);
// 	double mode = driveMode ? 1 : 0;
// 	table.getEntry("camMode").setNumber(mode);
// }
// public static void ledMode(boolean on){
// 	double mode = on ? 0 : 1;
// 	NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(mode);
// }
// public enum VisionModes{
// 	LOW(0),
// 	LEFT(1),
// 	RIGHT(2);
// 	public double mode;
// 	VisionModes(double mode){
// 		this.mode = mode;
// 	}
// }
// public static void setVisionMode(){
// 	setVisionMode(VisionModes.LOW);
// }
// public static void setVisionMode(VisionModes visionMode){
// 	table.getEntry("pipeline").setNumber(visionMode.mode);
// }
// public static String getVisionMode(){
// 	return  NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getString("0");
// }

// }
// // @Override
// // public void update(boolean isEnabled) {
// // 	SmartDashboard.putNumber("limelight x", getX());
// // 	SmartDashboard.putNumber("limelight y", getY());
// // 	SmartDashboard.putNumber("limelight area", getArea());
// // 	SmartDashboard.putNumber("limelight distance", getDistance());
// // 	SmartDashboard.putBoolean("limelight has target", isTargetAvalible());
// // 	SmartDashboard.putString("limelight mode", getVisionMode());
// // }