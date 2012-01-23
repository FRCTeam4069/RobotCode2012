package frc.t4069.utils.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

// See http://ideone.com/9dM5E
// For some reason they're using the byte reading/writing.
// TODO: Investigate further.

/**
 * Communications link back to another laptop. Useful for image processing.
 */
public class CommLink {
	public final static String SERVER_LOCATION = "socket://10.40.69.6:8888";

	private SocketConnection m_socket;
	private DataInputStream m_inputStream;
	private DataOutputStream m_outputStream;

	public CommLink() {
		this(SERVER_LOCATION);
	}

	public CommLink(String server) {
		try {
			m_socket = (SocketConnection) Connector.open(server);
			m_inputStream = m_socket.openDataInputStream();
			m_outputStream = m_socket.openDataOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	Intent read() {
		String message = null;
		try {
			message = m_inputStream.readUTF();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Intent obj = null;
		try {
			obj = (Intent) new JSONObject(message); // This must have an id
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	void write(Intent obj) {
		String message = obj.toString();
		try {
			m_outputStream.writeUTF(message);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}