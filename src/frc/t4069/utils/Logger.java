package frc.t4069.utils;

/**
 * Shortcut for logging.
 * 
 * @author shuhao
 * 
 */
public class Logger {

	public final static int DEBUG = 0;
	public final static int INFO = 1;
	public final static int WARNING = 2;
	public final static int ERROR = 3;
	public final static int WTF = 4;
	public final static int IGNORE_ALL = 5;

	private static int m_level = 0;

	/**
	 * Sets the maximum level to print to the console. The constants are the
	 * minimum level required to display.
	 * 
	 * Example: setLevel(WARNING) will only send messages logged with .w() and
	 * up
	 * 
	 * @param level
	 */
	public final static void setLevel(int level) {
		m_level = level;
	}

	/**
	 * Sends a debug message
	 * 
	 * @param msg Debug message
	 */
	public final static void d(String msg) {
		if (m_level <= DEBUG) Logger.output("Debug", msg);
	}

	/**
	 * Sends an info message.
	 * 
	 * @param msg
	 */
	public final static void i(String msg) {
		if (m_level <= INFO) Logger.output("Info", msg);
	}

	/**
	 * Sends a warning message
	 * 
	 * @param msg
	 */
	public final static void w(String msg) {
		if (m_level <= WARNING) Logger.output("Warning", msg);
	}

	/**
	 * Sends an error message
	 * 
	 * @param msg
	 */
	public final static void e(String msg) {
		if (m_level <= ERROR) Logger.output("Error", msg);
	}

	/**
	 * Sends a what a terrible failure message.
	 * 
	 * @param msg
	 */
	public final static void wtf(String msg) {
		if (m_level <= WTF) Logger.output("WTF", msg);
	}

	private final static void output(String prefix, String msg) {
		msg = "[" + prefix + "] " + msg;
		System.out.println(msg);
	}
}
