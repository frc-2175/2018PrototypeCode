package org.usfirst.frc.team2175.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LocTracker {
	private double theta;
	private double leftX;
	private double leftY;
	private double rightX;
	private double rightY;

	private double ticksToRadians;
	private double widthOfBot;

	private double leftEncDis;
	private double rightEncDis;

	public LocTracker(AHRS navX) {
		ticksToRadians = 1;
		widthOfBot = 30;
		theta = navX.getAngle();
		leftX = 0;
		leftY = 0;
		rightX = leftX;
		rightY = leftY + widthOfBot;
		leftEncDis = 0;
		rightEncDis = 0;
	}

	public void update(Encoder leftEncoder, Encoder rightEncoder, AHRS navXGyro) {
		double dL = leftEncoder.getDistance() - leftEncDis;
		leftEncDis = leftEncoder.getDistance();
		double dR = rightEncoder.getDistance() - rightEncDis;
		rightEncDis = rightEncoder.getDistance();
		if (dL == 0 && dR == 0) {
		} else if (Math.abs(dL - dR) < 0.125) {
			double angle = Math.toRadians(navXGyro.getAngle());
			double xDiff = dL * Math.cos(angle);
			double yDiff = dR * Math.sin(angle);
			leftX += xDiff;
			leftY += yDiff;
			rightX += xDiff;
			rightY += yDiff;
		} else {
			double dTheta = (dL - dR) / widthOfBot;
			double leftRadius = widthOfBot * dL / (dL - dR);
			double goingRelativeLeft = Math.signum(leftRadius);
			leftRadius = Math.abs(leftRadius);
			double rightRadius = leftRadius + -goingRelativeLeft * widthOfBot;
			double angle = Math.toRadians(navXGyro.getAngle());
			double leftXDiff = leftRadius * Math.cos(Math.toRadians(dTheta) + angle);
			double leftYDiff = leftRadius * Math.sin(Math.toRadians(dTheta) + angle);
			double rightXDiff = rightRadius * Math.cos(Math.toRadians(dTheta) + angle);
			double rightYDiff = rightRadius * Math.sin(Math.toRadians(dTheta) + angle);
			leftX += leftXDiff;
			leftY += leftYDiff;
			rightX += rightXDiff;
			rightY += rightYDiff;
		}

		theta = navXGyro.getAngle();

		SmartDashboard.putNumber("LeftX", leftX);
		SmartDashboard.putNumber("RightX", rightX);
		SmartDashboard.putNumber("LeftY", leftY);
		SmartDashboard.putNumber("RightY", rightY);
		SmartDashboard.putNumber("Theta", theta);
		SmartDashboard.putNumber("", 0);

	}

}
