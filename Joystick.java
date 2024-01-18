package frc.robot;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  TalonSRX _talon0 = new TalonSRX(1);
  TalonSRX _talon1 = new TalonSRX(3);
  TalonSRX _talon2 = new TalonSRX(5);
  TalonSRX _talon3 = new TalonSRX(2);
  TalonSRX _talon4 = new TalonSRX(4);
  TalonSRX _talon5 = new TalonSRX(6);
  Joystick _joystick = new Joystick(0);

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
