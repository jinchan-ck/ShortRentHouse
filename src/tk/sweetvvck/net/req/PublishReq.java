package tk.sweetvvck.net.req;

import tk.sweetvvck.entity.HouseInfo;

public class PublishReq extends BaseReq {
	
	private HouseInfo publishContent;

	public HouseInfo getPublishContent() {
		return publishContent;
	}

	public void setPublishContent(HouseInfo publishContent) {
		this.publishContent = publishContent;
	}
	
}
