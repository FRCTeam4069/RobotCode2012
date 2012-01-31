package frc.t4069.utils;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import frc.t4069.utils.networking.CommLink;

public class Logger {
	public final static boolean USE_MEDIATOR = false;

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
		msg = "[" + prefix + "] " + msg;
		System.out.println(msg);
		if (USE_MEDIATOR) {
			JSONObject o = new JSONObject();
			try {
				o.put("data", msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			CommLink.addKV("debugoutput", o);
		}
	}
}
