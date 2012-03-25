package frc.t4069.utils.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import edu.wpi.first.wpilibj.Timer;
import frc.t4069.utils.Logger;

public class CommLink {
	public static final String DEFAULT_URL = "http://10.40.69.6:4069/";
	public static final String USER_AGENT = "Team4069Robot/1.1";
	public static long timeout = 250; // Time out in miliseconds.

	public static int consecutiveFailures = 0;
	public static final int FAILURE_THRESHOLD = 5;

	private CommLink() {

	}

	public static class ReturnValue {
		public int responseCode;
		public JSONObject value;
	}

	private static abstract class CommThread extends Thread {
		protected String m_url = null;
		protected String m_data = null;
		protected String m_method = HttpConnection.GET;

		public JSONObject ret = null;
		public int responseCode = -1337;

		public CommThread(String url) {
			m_url = url;
		}

		public CommThread(String url, String data) {
			m_url = url;
			m_data = data;
			m_method = HttpConnection.POST;
		}

		protected abstract ReturnValue process(HttpConnection conn)
				throws IOException;

		public void run() {
			HttpConnection conn = null;
			InputStream input = null;
			OutputStream output = null;

			try {

				conn = (HttpConnection) Connector.open(m_url);
				conn.setRequestMethod(m_method);
				conn.setRequestProperty("User-Agent", USER_AGENT);

				ReturnValue rv = process(conn);

				if (rv.responseCode == HttpConnection.HTTP_OK) ret = rv.value;
				responseCode = rv.responseCode;

			} catch (IOException ex) {
				responseCode = -9999;
				Logger.w("CommLink Error: " + ex.getMessage());
			} finally {
				try {
					if (conn != null) conn.close();
					if (output != null) output.close();
					if (input != null) input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class PostThread extends CommThread {

		public PostThread(String url, String data) {
			super(url, data);
		}

		public static boolean post(String url, JSONObject data) {
			if (consecutiveFailures > FAILURE_THRESHOLD) return false;
			String jsonString = data.toString();

			PostThread pt = new PostThread(url, jsonString);
			pt.start();
			long startTime = new Date().getTime();
			boolean running = true;
			while (new Date().getTime() - startTime < timeout && running) {
				switch (pt.responseCode) {
					case -1337:
						continue;
					case 200:
						requestStatus(true);
						return true;
					case -9999:
						running = false;
					break;
					default:
						requestStatus(true);
						return false;
				}
				Timer.delay(0.025);
			}

			pt.interrupt();
			Logger.w("Timeout! Cannot reach server at " + url);
			requestStatus(false);
			return false;
		}

		protected ReturnValue process(HttpConnection conn) throws IOException {
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length",
					String.valueOf(m_data.getBytes().length));
			OutputStream output = conn.openOutputStream();
			output.write(m_data.getBytes());
			ReturnValue ret = new ReturnValue();
			ret.responseCode = conn.getResponseCode();
			return ret;
		}
	}

	private static class GetThread extends CommThread {
		public GetThread(String url) {
			super(url);
		}

		public static JSONObject get(String url) {
			if (consecutiveFailures > FAILURE_THRESHOLD) return null;
			GetThread ct = new GetThread(url);
			ct.start();
			long startTime = new Date().getTime();
			boolean running = true;
			while (new Date().getTime() - startTime < timeout && running) {
				switch (ct.responseCode) {
					case -1337:
						continue;
					case 200:
						requestStatus(true);
						return ct.ret;
					case -9999:
						running = false;
					break;
					default:
						requestStatus(true);
						return null;
				}
				Timer.delay(0.025);
			}

			ct.interrupt();
			Logger.w("Timeout! Cannot reach server at " + url);
			requestStatus(false);

			return null;
		}

		protected ReturnValue process(HttpConnection conn) throws IOException {
			JSONObject value = null;
			ReturnValue ret = new ReturnValue();
			StringBuffer strbuff = new StringBuffer();
			InputStream input = conn.openInputStream();
			ret.responseCode = conn.getResponseCode();
			if (ret.responseCode == HttpConnection.HTTP_OK) {
				int ch = 0;
				while (ch != -1) {
					ch = input.read();
					strbuff.append((char) ch);
				}
				try {
					value = new JSONObject(strbuff.toString());
				} catch (JSONException ex) {
					ret.responseCode = -400;
					Logger.wtf("Malformed JSON retrieved from server: "
							+ strbuff.toString());
				}
			}
			ret.value = value;
			return ret;
		}
	}

	private static void requestStatus(boolean success) {
		if (success)
			consecutiveFailures = 0;
		else
			consecutiveFailures++;
	}

	public static JSONObject getCommand() {
		return getCommand(DEFAULT_URL);
	}

	public static JSONObject getCommand(String url) {
		url += "q/commands";
		return GetThread.get(url);
	}

	public static boolean putData(JSONObject data) {
		return putData(DEFAULT_URL, data);
	}

	public static boolean putData(String url, JSONObject data) {
		url += "q/data";
		return PostThread.post(url, data);
	}

	public static boolean addKV(String key, JSONObject value) {
		return addKV(DEFAULT_URL, key, value);
	}

	public static boolean addKV(String url, String key, JSONObject value) {
		url += "kv/" + key;
		return PostThread.post(url, value);
	}

	public static JSONObject getKV(String key) {
		return getKV(DEFAULT_URL, key);
	}

	public static JSONObject getKV(String url, String key) {
		url += "kv/" + key;
		return GetThread.get(url);
	}

	public static void deleteKV(String key) {
		deleteKV(DEFAULT_URL, key);
	}

	public static void deleteKV(String url, String key) {
		url += "delkv/" + key;
		GetThread.get(url);
	}

	public static boolean addKV(String key, String value) {
		JSONObject o = new JSONObject();
		try {
			o.put("data", value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return addKV(key, o);
	}
}
