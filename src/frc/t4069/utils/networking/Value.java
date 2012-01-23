package frc.t4069.utils.networking;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

public class Value extends JSONObject {
	public Value() {
		super();
		try {
			this.put("error", 0);
		} catch (JSONException e) {
			// Should never happen
			e.printStackTrace();
		}
	}

	public Value(JSONTokener x) throws JSONException {
		super(x);
		this.put("error", 0);
	}

	public Value(String x) throws JSONException {
		super(x);
		this.put("error", 0);
	}

	public int getError() {
		try {
			return this.getInt("error");
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
