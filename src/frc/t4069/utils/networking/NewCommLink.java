package frc.t4069.utils.networking;

import frc.t4069.utils.Queue;

public class NewCommLink {
	public static final String DEFAULT_URL = "http://10.40.69.6:4069/";
	public static final String USER_AGENT = "Team4069Robot/2.0";

	private static class CommThread {
		public Queue messageQueue;

		public CommThread() {
			messageQueue = new Queue();
		}
	}

	private static CommThread m_ct;

}
