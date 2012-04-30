package frc.t4069.utils;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * Sonar class that's never really tested.
 * 
 * @author Shuhao
 * 
 */
public class MaxbotixSonar {

	private AnalogChannel m_sonar;

	public MaxbotixSonar(int channel) {
		m_sonar = new AnalogChannel(channel);
	}

	public MaxbotixSonar(AnalogChannel channel) {
		m_sonar = channel;
	}

	public double getVoltage() {
		return m_sonar.getVoltage();
	}

	public double getDistance() {
		return getDistance(false);
	}

	public double getDistance(boolean inches) {
		double multiplier;
		if (inches)
			multiplier = 1.0;
		else
			multiplier = 2.54;

		return getVoltage() / 0.009766 * multiplier;
	}
}
