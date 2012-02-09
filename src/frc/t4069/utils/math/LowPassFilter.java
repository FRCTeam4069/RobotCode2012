package frc.t4069.utils.math;

import java.util.Date;

public class LowPassFilter {
	private double m_lastValue = 0;
	private long m_lastTime = -1;
	private double m_RC;

	public LowPassFilter(double RC) {
		m_RC = RC;
	}

	public void setRC(double RC) {
		m_RC = RC;
	}

	public double getRC() {
		return m_RC;
	}

	public double calculate(double value) {
		if (m_lastTime > 0) {
			long currentTime = new Date().getTime();
			double a = currentTime - m_lastTime;
			a /= (a + m_RC);
			m_lastTime = currentTime;
			m_lastValue = a * value + (1 - a) * m_lastValue;
		} else
			m_lastTime = new Date().getTime();

		return m_lastValue;
	}
}
