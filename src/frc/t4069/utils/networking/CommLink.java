package frc.t4069.utils.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import frc.t4069.utils.Logger;

public class CommLink {
	public static final String DEFAULT_URL = "http://10.40.69.6:4069/";
	public static final String USER_AGENT = "Team4069Robot/1.0";

	private CommLink() {

	}

	public static Value getCommand() throws IOException {
		return getCommand(DEFAULT_URL);
	}

	private static Value get(String url) {
		HttpConnection c = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		Value value = null;

		try {
			c = (HttpConnection) Connector.open(url);
			c.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = c.getResponseCode();
			switch (responseCode) {
				case HttpConnection.HTTP_OK:
					is = c.openInputStream();
					int ch = 0;
					while (ch != -1) {
						ch = is.read();
						sb.append((char) ch);
					}
					value = new Value(sb.toString());
				break;
				case HttpConnection.HTTP_NOT_FOUND:
					value = new Value();
					value.put("error", 404);
				break;
				default:
					throw new IOException("Error: " + responseCode);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			Logger.wtf("JSON Failure: " + sb.toString());
		}

		return value;
	}

	public static Value getCommand(String url) throws IOException {
		url += "q/commands";
		return get(url);
	}

	public static boolean putData(JSONObject data) throws IOException {
		return putData(DEFAULT_URL, data);
	}

	public static boolean putData(String url, JSONObject data)
			throws IOException {
		url += "q/data";
		String jsonString = data.toString();

		if (send(url, jsonString) != HttpConnection.HTTP_OK)
			return false;
		else
			return true;
	}

	private static int send(String url, String data) {
		HttpConnection c = null;
		OutputStream os = null;
		int rc = -1;
		try {
			c = (HttpConnection) Connector.open(url);

			os = c.openOutputStream();
			c.setRequestProperty("Content-Type", "application/json");
			c.setRequestProperty("User-Agent", USER_AGENT);
			c.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));
			c.setRequestMethod(HttpConnection.POST);
			os.write(data.getBytes());
			rc = c.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (c != null) c.close();
				if (os != null) os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rc;
	}

	public static boolean addKV(String key, JSONObject value) {
		return addKV(DEFAULT_URL, key, value);
	}

	public static boolean addKV(String url, String key, JSONObject value) {
		url += "kv/" + key;
		String jsonString = value.toString();
		if (send(url, jsonString) != HttpConnection.HTTP_OK)
			return false;
		else
			return true;
	}

	public static Value getKV(String key) {
		return getKV(DEFAULT_URL, key);
	}

	public static Value getKV(String url, String key) {
		url += "kv/" + key;
		return get(url);
	}

	public static boolean deleteKV(String key) {
		return deleteKV(DEFAULT_URL, key);
	}

	public static boolean deleteKV(String url, String key) {
		url += "delkv/" + key;
		Value v = get(url);
		if (v != null && v.getError() == 0)
			return true;
		else
			return false;
	}
}
