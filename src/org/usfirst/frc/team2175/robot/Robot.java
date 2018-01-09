/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2175.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	private LocTracker locTracker;
	private boolean shouldLocTrackerReset;

	private WPI_TalonSRX leftDriveMaster;
	private WPI_TalonSRX leftDriveSlave1;
	private WPI_TalonSRX leftDriveSlave2;
	private WPI_TalonSRX rightDriveMaster;
	private WPI_TalonSRX rightDriveSlave1;
	private WPI_TalonSRX rightDriveSlave2;

	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private WPI_TalonSRX rollerBar;
	private Joystick rightJoystick;
	private Joystick leftJoystick;
	private Joystick gamepad;

	private DifferentialDrive robotDrive;

	private AHRS navXGyro;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		leftDriveMaster = new WPI_TalonSRX(1);
		leftDriveSlave1 = new WPI_TalonSRX(3);
		leftDriveSlave2 = new WPI_TalonSRX(4);
		rightDriveMaster = new WPI_TalonSRX(2);
		rightDriveSlave1 = new WPI_TalonSRX(5);
		rightDriveSlave2 = new WPI_TalonSRX(6);
		leftEncoder = new Encoder(0, 1);
		rightEncoder = new Encoder(2, 3);

		navXGyro = new AHRS(SPI.Port.kMXP);

		locTracker = new LocTracker();
		shouldLocTrackerReset = false;

		rollerBar = new WPI_TalonSRX(14);
		leftDriveSlave1.follow(leftDriveMaster);
		leftDriveSlave2.follow(leftDriveMaster);
		rightDriveSlave1.follow(rightDriveMaster);
		rightDriveSlave2.follow(rightDriveMaster);
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		gamepad = new Joystick(2);
		robotDrive = new DifferentialDrive(leftDriveMaster, rightDriveMaster);
	}

	@Override
	public void autonomousInit() {

		locTracker = new LocTracker();

		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	@Override
	public void autonomousPeriodic() {
		locTracker.update(leftEncoder, rightEncoder, navXGyro);

		switch (m_autoSelected) {
		case kCustomAuto:
			// Put custom auto code here
			break;
		case kDefaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	@Override
	public void teleopInit() {
		if (shouldLocTrackerReset) {
			locTracker = new LocTracker();
		}
		shouldLocTrackerReset = false;
	}

	@Override
	public void teleopPeriodic() {
		shouldLocTrackerReset = true;
		robotDrive.arcadeDrive(leftJoystick.getY(), rightJoystick.getX());
		if (gamepad.getRawButton(8)) {
			rollerBar.set(1 - leftJoystick.getRawAxis(2));
		}

		locTracker.update(leftEncoder, rightEncoder, navXGyro);
	}

	@Override
	public void testPeriodic() {
	}
}
