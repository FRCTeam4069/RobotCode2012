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

	private static final String COMMAND_POSTFIX = "q/commands";

	private CommLink() {

	}

	public static JSONObject getCommand() throws IOException {
		return getCommand(DEFAULT_URL);
	}

	public static JSONObject getCommand(String url) throws IOException {
		HttpConnection c = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		JSONObject jo;
		int responseCode;
		url += "q/commands";

		try {
			c = (HttpConnection) Connector.open(url);
			c.setRequestProperty("User-Agent", USER_AGENT);

			responseCode = c.getResponseCode();
			switch (responseCode) {
				case HttpConnection.HTTP_OK:
					is = c.openInputStream();
					int ch = 0;
					while (ch != -1) {
						ch = is.read();
						sb.append(ch);
					}
					jo = new JSONObject(sb.toString());
					return jo;
				case HttpConnection.HTTP_NOT_FOUND:
					return null;
				default:
					throw new IOException("Error: " + responseCode);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (c != null) c.close();
			if (is != null) is.close();
		}
		return null;
	}

	public static boolean putData(JSONObject data) throws IOException {
		return putData(DEFAULT_URL, data);
	}

	public static boolean putData(String url, JSONObject data)
			throws IOException {
		url += "q/data";
		InputStream is = null;
		OutputStream os = null;
		HttpConnection c = null;
		String jsonString = data.toString();
		Logger.d(jsonString);

		try {
			c = (HttpConnection) Connector.open(url);
			c.setRequestProperty("Contennt-Type", "application/json");
			c.setRequestProperty("User-Agent", USER_AGENT);
			c.setRequestProperty("Content-Length",
					String.valueOf(jsonString.getBytes().length));
			c.setRequestMethod(HttpConnection.POST);
			os = c.openOutputStream();
			os.write(jsonString.getBytes());
			int responseCode = c.getResponseCode();
			if (responseCode != HttpConnection.HTTP_OK)
				return false;
			else
				return true;

		} finally {
			if (is != null) is.close();
			if (os != null) os.close();
			if (c != null) c.close();
		}
	}
}
