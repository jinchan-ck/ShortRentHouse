package tk.sweetvvck.net.req;

import java.io.Serializable;

public class BaseReq implements Serializable{
	private static final long serialVersionUID = 1L;
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
