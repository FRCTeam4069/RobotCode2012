package frc.t4069.utils;

public class Logger {
	public final static void d(String msg) {
		Logger.output("Debug", msg);
	}

	public final static void i(String msg) {
		Logger.output("Info", msg);
	}

	public final static void w(String msg) {
		Logger.output("Warning", msg);
	}

	public final static void e(String msg) {
		Logger.output("Error", msg);
	}

	public final static void wtf(String msg) {
		Logger.output("WTF", msg);
	}

	private final static void output(String prefix, String msg) {
		System.out.println("[" + prefix + "] " + msg);
	}
}
