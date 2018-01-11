package org.usfirst.frc.team2175.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LocTracker {
	private double theta;
	private double botX;
	private double botY;

	private double inchesToTicks = 48.333;
	private final double widthOfBot = inchesToTicks * 31.25;
	private final double lengthOfBot = inchesToTicks * 28.25;

	private double leftEncDis;
	private double rightEncDis;

	public LocTracker(AHRS navX) {
		botX = widthOfBot / 2;
		botY = lengthOfBot / 2;
		leftEncDis = 0;
		rightEncDis = 0;
		theta = 0;
	}

	public void update(Encoder leftEncoder, Encoder rightEncoder, AHRS navXGyro) {

		// Find change of encoders over the tick change
		double dL = leftEncoder.getDistance() - leftEncDis;
		leftEncDis = leftEncoder.getDistance();
		double dR = rightEncoder.getDistance() - rightEncDis;
		rightEncDis = rightEncoder.getDistance();

		theta = navXGyro.getAngle();
		double angle = Math.toRadians(theta);

		if (dL == 0 && dR == 0) { // If robot did not move, do nothing

		} else if (Math.abs(dL - dR) <= 1) { // if the robot went straight

			double avgDis = (dL + dR) / 2;
			double xDiff = avgDis * Math.cos(angle);
			double yDiff = avgDis * Math.sin(angle);
			botX += xDiff;
			botY += yDiff;
			SmartDashboard.putNumber("StraightxDiff", xDiff);
			SmartDashboard.putNumber("StraightyDiff", yDiff);

		} else { // if the robot moved and did not go straight
			double dTheta = (dL - dR) / widthOfBot;
			double centerRadius = widthOfBot * (dL / (dL - dR) - 1 / 2);

			double xDiff = centerRadius * (Math.cos(dTheta + angle) - Math.cos(angle));
			double yDiff = centerRadius * (Math.sin(dTheta + angle) - Math.sin(angle));

			botX += xDiff;
			botY += yDiff;

			SmartDashboard.putNumber("centerRadius", centerRadius);
			SmartDashboard.putNumber("xDiff", xDiff);
			SmartDashboard.putNumber("yDiff", yDiff);
			SmartDashboard.putNumber("dL", dL);
			SmartDashboard.putNumber("dR", dR);
			SmartDashboard.putNumber("dTheta", dTheta);
		}

		SmartDashboard.putNumber("botX", botX);
		SmartDashboard.putNumber("botY", botY);
		SmartDashboard.putNumber("Theta", theta);
		SmartDashboard.putNumber("LeftEncoder", leftEncoder.getDistance());
		SmartDashboard.putNumber("RightEncoder", rightEncoder.getDistance());

	}

}
