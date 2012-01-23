package frc.t4069.utils.networking;

import org.json.me.JSONObject;
import org.json.me.JSONException;
import org.json.me.JSONTokener;

public class Intent extends JSONObject{

	public final void setID(String id) throws JSONException {
		this.put("id", id);
	}

	public Intent(String id) throws JSONException {
		super();
		setID(id);
	}

	public Intent(String id, JSONTokener x) throws JSONException {
		super(x);
		setID(id);
	}

	public Intent(String id, String string) throws JSONException {
		super(string);
		setID(id);
	}

}
