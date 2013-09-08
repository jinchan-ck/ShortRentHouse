package tk.sweetvvck.net.req;

import java.io.Serializable;

import tk.sweetvvck.entity.HouseInfo;

public class PublishReq extends BaseReq implements Serializable{
	private static final long serialVersionUID = 1L;
	private HouseInfo publishContent;

	public HouseInfo getPublishContent() {
		return publishContent;
	}

	public void setPublishContent(HouseInfo publishContent) {
		this.publishContent = publishContent;
	}
	
}
