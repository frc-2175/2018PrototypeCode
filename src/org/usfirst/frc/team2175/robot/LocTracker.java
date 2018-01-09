package org.usfirst.frc.team2175.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;

public class LocTracker {
	private double thetaAccumulation;
	private double theta;
	private double xCoord;
	private double yCoord;

	private double ticksToRadians;
	private double widthOfBot;

	private double leftEncDis;
	private double rightEncDis;

	public LocTracker() {
		thetaAccumulation = 0;
		ticksToRadians = 1;
		widthOfBot = 30;
		theta = 0;
		xCoord = 0;
		yCoord = 0;
		leftEncDis = 0;
		rightEncDis = 0;
	}

	public void update(Encoder leftEncoder, Encoder rightEncoder, AHRS navXGyro) {
		double dL = leftEncoder.getDistance() - leftEncDis;
		leftEncDis = leftEncoder.getDistance();
		double dR = rightEncoder.getDistance() - rightEncDis;
		if (dL == 0 && dR == 0) {
		} else if (dL - dR == 0) {

		}
		rightEncDis = rightEncoder.getDistance();
		double dTheta = (dL - dR) / widthOfBot;
		double leftRadius = widthOfBot * dL / (dL - dR);

		theta = navXGyro.getAngle();
	}

}
