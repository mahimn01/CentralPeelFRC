#!/usr/bin/env python3
import wpilib
from wpilib.drive import DifferentialDrive
from wpilib import XboxController, Timer
from wpilib import SendableChooser, SmartDashboard
import networktables

class Robot(wpilib.TimedRobot):
    def robotInit(self):
        # Constants
        self.kDefaultAuto = "Default"
        self.kCustomAuto = "My Auto"

        # Motor controllers
        self.talon0 = wpilib.Talon(1)
        self.talon1 = wpilib.Talon(3)
        self.talon2 = wpilib.Talon(5)
        self.talon3 = wpilib.Talon(2)
        self.talon4 = wpilib.Talon(4)
        self.talon5 = wpilib.Talon(6)

        def set_left_motors(speed):
            self.talon0.set(speed)
            self.talon1.set(speed)
            self.talon2.set(speed)

        def set_right_motors(speed):
            self.talon3.set(speed)
            self.talon4.set(speed)
            self.talon5.set(speed)

        # Drivetrain
        self.drive = DifferentialDrive(set_left_motors, set_right_motors)

        # Controllers
        self.controller = XboxController(0)

        # Limelight variables
        self.limelight_has_valid_target = False
        self.limelight_drive_command = 0.0
        self.limelight_steer_command = 0.0

        # Autonomous variables
        self.auto_timer = Timer()
        self.auto_time = 15.0
        self.target_distance = 5.0
        self.reached_target = False

        # Network Tables for Limelight
        self.limelight_table = networktables.NetworkTablesInstance.getDefault().getTable("limelight")

        # Autonomous selection
        self.chooser = SendableChooser()
        self.chooser.setDefaultOption("Default Auto", self.kDefaultAuto)
        self.chooser.addOption("My Auto", self.kCustomAuto)
        SmartDashboard.putData("Auto choices", self.chooser)

    def autonomousInit(self):
        self.auto_timer.reset()
        self.auto_timer.start()
        self.reached_target = False

        self.talon0.setInverted(False)
        self.talon2.setInverted(False)
        self.talon4.setInverted(False)
        self.talon1.setInverted(True)
        self.talon3.setInverted(True)
        self.talon5.setInverted(True)

        self.auto_selected = self.chooser.getSelected()

    def teleopPeriodic(self):
        self.update_limelight_tracking()

        steer = self.controller.getRawAxis(4) * 0.3
        drive = self.controller.getRawAxis(1) * 0.3
        auto = self.controller.getAButton()

        if auto:
            if self.limelight_has_valid_target:
                left = self.limelight_drive_command + self.limelight_steer_command
                right = self.limelight_drive_command - self.limelight_steer_command

                self.talon0.set(left)
                self.talon1.set(left)
                self.talon2.set(left)
                self.talon3.set(-right)
                self.talon4.set(-right)
                self.talon5.set(-right)
            else:
                self.talon0.set(0)
                self.talon1.set(0)
                self.talon2.set(0)
                self.talon3.set(0)
                self.talon4.set(0)
                self.talon5.set(0)
        else:
            left = drive + steer
            right = drive - steer

            self.talon0.set(left)
            self.talon1.set(left)
            self.talon2.set(left)
            self.talon3.set(-right)
            self.talon4.set(-right)
            self.talon5.set(-right)

    def update_limelight_tracking(self):

        STEER_K = 0.03  # How hard to turn toward the target
        DRIVE_K = 0.26  # How hard to drive forward
        DESIRED_TARGET_AREA = 13.0  # Target area when the robot is at the wall
        MAX_DRIVE = 0.7  # Limit on drive speed

        # Get Limelight values (Check if the entry names match your setup)
        tv = self.limelight_table.getEntry("tv").getDouble(0.0)
        tx = self.limelight_table.getEntry("tx").getDouble(0.0)
        ty = self.limelight_table.getEntry("ty").getDouble(0.0)
        ta = self.limelight_table.getEntry("ta").getDouble(0.0)

        # Check for valid target
        if tv < 1.0:
            self.limelight_has_valid_target = False
            self.limelight_drive_command = 0.0
            self.limelight_steer_command = 0.0
            return  # Stop execution if no target is found

        # Target is found!
        self.limelight_has_valid_target = True

        # Calculate steering command
        steer_cmd = tx * STEER_K
        self.limelight_steer_command = steer_cmd

        # Calculate drive command
        drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K
        if drive_cmd > MAX_DRIVE:
            drive_cmd = MAX_DRIVE
        self.limelight_drive_command = drive_cmd

if __name__ == "__main__":
    wpilib.run(Robot)
