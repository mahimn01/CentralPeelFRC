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

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
// import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.*;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    // Motor controllers

    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";

    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();


    TalonSRX _talon0 = new TalonSRX(1);
    TalonSRX _talon1 = new TalonSRX(3);

    TalonSRX _talon2 = new TalonSRX(5);
    TalonSRX _talon3 = new TalonSRX(2);
    TalonSRX _talon4 = new TalonSRX(4);
    TalonSRX _talon5 = new TalonSRX(6);

    private MotorController _leftMotors = new MotorController() {
        @Override
        public void set(double speed) {
            _talon0.set(ControlMode.PercentOutput, speed);
            _talon1.set(ControlMode.PercentOutput, speed);
            _talon1.set(ControlMode.PercentOutput, speed);
        }

        @Override
        public void setInverted(boolean isInverted) {
            _talon0.setInverted(false);
            _talon1.setInverted(false);
            _talon2.setInverted(false);
        }

        @Override
        public boolean getInverted() {
            return _talon0.getInverted();
        }

        @Override
        public void disable() {
        }

        @Override
        public void stopMotor() {
            _talon0.set(ControlMode.PercentOutput, 0);
            _talon1.set(ControlMode.PercentOutput, 0);
            _talon2.set(ControlMode.PercentOutput, 0);
        }

        @Override
        public double get() {
            return _talon0.getMotorOutputPercent();
        }
    };

    private MotorController _rightMotors = new MotorController() {
        @Override
        public void set(double speed) {
            _talon3.set(ControlMode.PercentOutput, speed);
            _talon4.set(ControlMode.PercentOutput, speed);
            _talon5.set(ControlMode.PercentOutput, speed);
        }

        @Override
        public void setInverted(boolean isInverted) {
            _talon3.setInverted(true);
            _talon4.setInverted(true);
            _talon5.setInverted(true);
        }

        @Override
        public boolean getInverted() {
            return _talon3.getInverted();
        }

        @Override
        public void disable() {
        }

        @Override
        public void stopMotor() {
            _talon3.set(ControlMode.PercentOutput, 0);
            _talon4.set(ControlMode.PercentOutput, 0);
            _talon5.set(ControlMode.PercentOutput, 0);
        }

        @Override
        public double get() {
            return _talon0.getMotorOutputPercent();
        }
    };

    private MotorControllerGroup m_LeftMotors = new MotorControllerGroup(_leftMotors);
    private MotorControllerGroup m_RightMotors = new MotorControllerGroup(_rightMotors);

    private DifferentialDrive m_Drive = new DifferentialDrive(m_LeftMotors,m_RightMotors);

    private XboxController m_Controller = new XboxController(0);

    private boolean m_LimelightHasValidTarget = false;
    private double m_LimelightDriveCommand = 0.0;
    private double m_LimelightSteerCommand = 0.0;

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

        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("My Auto", kCustomAuto);
        SmartDashboard.putData("Auto choices", m_chooser);
    }

    @Override
    public void robotPeriodic() {
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

        m_autoSelected = m_chooser.getSelected();
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
                double drive = -0.02 * targetY;
                drive = Math.max(drive, 0.4);

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
    public void teleopPeriodic() {
        Update_Limelight_Tracking();

        double steer = m_Controller.getRawAxis(4) * 0.3;
        double drive = m_Controller.getRawAxis(1) * 0.3;
        boolean auto = m_Controller.getAButton();

        steer *= 0.70;
        drive *= 0.70;

        if (auto)
        {
            if (m_LimelightHasValidTarget)
            {
                    // m_Drive.arcadeDrive(m_LimelightDriveCommand,m_LimelightSteerCommand);

                    double left = m_LimelightDriveCommand + m_LimelightSteerCommand;
                    double right = m_LimelightDriveCommand - m_LimelightSteerCommand;

                    _leftMotors.set(left);
                    _rightMotors.set(right);
            }
            else
            {
                    // m_Drive.arcadeDrive(0.0,0.0);
                    _leftMotors.set(0);
                    _rightMotors.set(0);
            }
        }
        else
        {
            double left = drive + steer;
            double right = drive - steer;
            
            _leftMotors.set(left);
            _rightMotors.set(-right);
        }
    }

    public void Update_Limelight_Tracking()
    {
        // These numbers must be tuned for your Robot!  Be careful!
        final double STEER_K = 0.03;                    // how hard to turn toward the target
        final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
        final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
        final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast

        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

        if (tv < 1.0)
        {
          m_LimelightHasValidTarget = false;
          m_LimelightDriveCommand = 0.0;
          m_LimelightSteerCommand = 0.0;
          return;
        }

        m_LimelightHasValidTarget = true;

        double steer_cmd = tx * STEER_K;
        m_LimelightSteerCommand = steer_cmd;

        double drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K;

        if (drive_cmd > MAX_DRIVE)
        {
          drive_cmd = MAX_DRIVE;
        }
        m_LimelightDriveCommand = drive_cmd;
  }
}