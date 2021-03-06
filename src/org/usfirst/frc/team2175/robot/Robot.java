/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2175.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
    private WPI_TalonSRX leftDriveMaster;
    private WPI_TalonSRX leftDriveSlave1;
    private WPI_TalonSRX leftDriveSlave2;
    private WPI_TalonSRX rightDriveMaster;
    private WPI_TalonSRX rightDriveSlave1;
    private WPI_TalonSRX rightDriveSlave2;
    private WPI_TalonSRX rollerBar, leftIntake, rightIntake;
    private DoubleSolenoid intake;
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    private Joystick gamepad;
    private DifferentialDrive robotDrive;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
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
        rollerBar = new WPI_TalonSRX(14);
        leftIntake = new WPI_TalonSRX(15);
        rightIntake = new WPI_TalonSRX(16);
        intake = new DoubleSolenoid(0, 1);
        leftDriveSlave1.follow(leftDriveMaster);
        leftDriveSlave2.follow(leftDriveMaster);
        rightDriveSlave1.follow(rightDriveMaster);
        rightDriveSlave2.follow(rightDriveMaster);
        leftJoystick = new Joystick(0);
        rightJoystick = new Joystick(1);
        gamepad = new Joystick(2);
        robotDrive = new DifferentialDrive(leftDriveMaster, rightDriveMaster);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        m_autoSelected = m_chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
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

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        robotDrive.curvatureDrive(leftJoystick.getY(), -rightJoystick.getX(), false);
        // robotDrive.tankDrive(leftJoystick.getY(), rightJoystick.getY());
        double barSpeed = 1 - leftJoystick.getRawAxis(2);
        double leftSpeed = 0;
        double rightSpeed = 0;
        if (gamepad.getRawAxis(0) < 0) {
            leftSpeed = gamepad.getRawAxis(0);
            rightSpeed = gamepad.getRawAxis(0) / 2;

        } else if (gamepad.getRawAxis(0) > 0) {
            leftSpeed = gamepad.getRawAxis(0) / 2;
            rightSpeed = gamepad.getRawAxis(0);
        } else {
            leftSpeed = 0;
            rightSpeed = 0;
        }

        if (gamepad.getRawButton(8)) {
            barSpeed *= -1;
            leftSpeed = -(leftJoystick.getThrottle() + 1) / 2;
            rightSpeed = (rightJoystick.getThrottle() + 1) / 2;
        } else if (gamepad.getRawButton(7)) {
            leftSpeed = (leftJoystick.getThrottle() + 1) / 2;
            rightSpeed = -(rightJoystick.getThrottle() + 1) / 2;
        } else {
            barSpeed = 0;
        }

        rollerBar.set(barSpeed);
        leftIntake.set(leftSpeed);
        rightIntake.set(rightSpeed);

        if (leftJoystick.getRawButton(1)) {
            intake.set(Value.kForward);
        } else if (rightJoystick.getRawButton(1)) {
            intake.set(Value.kReverse);
        }
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
