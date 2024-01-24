package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.helpers.LimelightHelpers;

public class LimeLight extends SubsystemBase {

    public boolean m_ValidTarget = false;
    public double tx;
    public double ty;
    public double tv;
    public double ta;
    private final Field2d m_field = new Field2d();


    public LimeLight(){
        //setup for calibration
        SmartDashboard.putData("Field", m_field);
    }

    @Override
    public void periodic() {
        updateLimeLightTracking();
    }

    public void updateLimeLightTracking() {
         tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
         tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
         ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
         ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

        if (tv < 1.0)
        {
          m_ValidTarget = false;
          return;
        }
        else {
            m_ValidTarget = true;
        }

        SmartDashboard.putNumber("LimeLight TV", tv);
        SmartDashboard.putNumber("LimeLight TX", tx);
        SmartDashboard.putNumber("LimeLight TY", ty);
        SmartDashboard.putNumber("LimeLight TA", ta);
        SmartDashboard.putBoolean("Target Aquired", m_ValidTarget);


        //post estimated field postion to smart dashboard
        Pose2d robotPose = LimelightHelpers.getBotPose2d("limelight");
        // Update Smart Dashboard with robot position
        SmartDashboard.putNumber("Robot X", robotPose.getX());
        SmartDashboard.putNumber("Robot Y", robotPose.getY());
        SmartDashboard.putNumber("Robot Orientation", robotPose.getRotation().getDegrees());

        //Post position to field map
        m_field.setRobotPose(robotPose);
    }
}
