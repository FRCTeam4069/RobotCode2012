package frc.t4069.utils.math;

import java.util.Date;

/**
 * An easy to use low pass filter.
 * 
 * @author shuhao
 * 
 */
public class LowPassFilter {
	private double m_lastValue = 0;
	private long m_lastTime = -1;
	private double m_RC;

	/**
	 * Resets the LowPassFilter.
	 */
	public void reset() {
		m_lastValue = 0;
		m_lastTime = -1;
	}

	/**
	 * Specifies an RC value and construct a new LowPassFilter object.
	 * 
	 * @param RC
	 */
	public LowPassFilter(double RC) {
		m_RC = RC;
	}

	/**
	 * Sets an RC value
	 * 
	 * @param RC
	 */
	public void setRC(double RC) {
		m_RC = RC;
	}

	/**
	 * Retrieves an RC value.
	 * 
	 * @return
	 */
	public double getRC() {
		return m_RC;
	}

	/**
	 * Call this everytime you need to calculate the value. Recommend you do
	 * this every iteration. Otherwise try to reset it.
	 * 
	 * @param value The value to go in
	 * @return The value the filter computes
	 */
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
