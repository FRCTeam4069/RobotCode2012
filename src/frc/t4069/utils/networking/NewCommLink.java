package frc.t4069.utils.networking;

public class NewCommLink {
	public static final String DEFAULT_URL = "http://10.40.69.6:4069/";
	public static final String USER_AGENT = "Team4069Robot/1.1";

	private String m_url;

	public NewCommLink() {
		this(DEFAULT_URL);
	}

	public NewCommLink(String url) {
		m_url = url;
	}
}
