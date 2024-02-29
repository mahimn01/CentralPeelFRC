package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class LimeLight extends SubsystemBase {

private static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
//x location of the target
private static NetworkTableEntry tx = table.getEntry("tx");
//y location of the target
private static NetworkTableEntry ty = table.getEntry("ty");
//area of the target
private static NetworkTableEntry ta = table.getEntry("ta");
//does the limelight have a target
private static NetworkTableEntry tv = table.getEntry("tv");

public static double getX(){
	return tx.getDouble(0.0);
}

public static double getY(){
	return ty.getDouble(0.0);
}

public static double getDistance(){
	double heightOfCamera = 43;
	double heightOfTarget = 29;
	double angleOfCamera = -20;
	double angleofTarget =  ty.getDouble(0.0);
	return (heightOfTarget - heightOfCamera) / Math.tan(Math.toRadians(angleOfCamera + angleofTarget));
}

public static double getArea(){
	return ta.getDouble(0.0);
}

public static boolean isTargetAvalible(){
	return tv.getBoolean(false);
}
//Moding the limelight to work for the individual case
public static void drivingMode(boolean driveMode){
	ledMode(!driveMode);
	double mode = driveMode ? 1 : 0;
	table.getEntry("camMode").setNumber(mode);
}
public static void ledMode(boolean on){
	double mode = on ? 0 : 1;
	NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(mode);
}
public enum VisionModes{
	LOW(0),
	LEFT(1),
	RIGHT(2);
	public double mode;
	VisionModes(double mode){
		this.mode = mode;
	}
}
public static void setVisionMode(){
	setVisionMode(VisionModes.LOW);
}
public static void setVisionMode(VisionModes visionMode){
	table.getEntry("pipeline").setNumber(visionMode.mode);
}
public static String getVisionMode(){
	return  NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getString("0");
}
}