package tk.sweetvvck.entity;

public class SinaResp {

	/*
	 * {"tinyurl":"http://tp2.sinaimg.cn/1621387877/50/5665212522/1","birthday":"0"
	 * ,
	 * "media_type":"sinaweibo","sex":"1","is_verified":"0","university_history"
	 * :[],"work_history":[],"mainurl":
	 * "http://tp2.sinaimg.cn/1621387877/50/5665212522/1"
	 * ,"expires_in":"625966","headurl"
	 * :"http://tp2.sinaimg.cn/1621387877/50/5665212522/1"
	 * ,"city":"朝阳区","hometown_location"
	 * :[],"username":"sweetvvck","province":"北京"
	 * ,"social_uid":3909405914,"access_token"
	 * :"50.994f3a95a784926bf5dd51a9bc2560b2.625966.1379012400.3909405914-391335"
	 * ,"media_uid":"1621387877","hs_history":[]}
	 */

	/**
	 * {"id":1621387877,"idstr":"1621387877","class":1,"screen_name":"sweetvvck"
	 * ,"name":"sweetvvck","province":"11","city":"5","location":"北京 朝阳区",
	 * "description":"","url":"","profile_image_url":
	 * "http://tp2.sinaimg.cn/1621387877/50/5665212522/1"
	 * ,"profile_url":"u/1621387877"
	 * ,"domain":"","weihao":"","gender":"m","followers_count"
	 * :11,"friends_count"
	 * :57,"statuses_count":103,"favourites_count":5,"created_at"
	 * :"Thu Jun 28 16:01:45 +0800 2012"
	 * ,"following":false,"allow_all_act_msg":false
	 * ,"geo_enabled":true,"verified"
	 * :false,"verified_type":-1,"remark":"","status"
	 * :{"created_at":"Thu Sep 05 20:36:16 +0800 2013"
	 * ,"id":3619269588081869,"mid"
	 * :"3619269588081869","idstr":"3619269588081869"
	 * ,"text":"友盟社会化组件可以让移动应用快速具备社会化分享、登录、评论、喜欢等功能，并提供实时、全面的社会化数据统计分析服务"
	 * ,"source":
	 * "<a href=\"http://app.weibo.com/t/feed/crXpu\" rel=\"nofollow\">友盟社会化组件</a>"
	 * ,"favorited":false,"truncated":false,"in_reply_to_status_id":"",
	 * "in_reply_to_user_id"
	 * :"","in_reply_to_screen_name":"","pic_urls":[],"geo":
	 * null,"reposts_count":
	 * 0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible"
	 * :{"type":0,"list_id"
	 * :0}},"ptype":0,"allow_all_comment":true,"avatar_large"
	 * :"http://tp2.sinaimg.cn/1621387877/180/5665212522/1"
	 * ,"avatar_hd":"http://tp2.sinaimg.cn/1621387877/180/5665212522/1"
	 * ,"verified_reason"
	 * :"","follow_me":false,"online_status":0,"bi_followers_count"
	 * :3,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0}
	 */

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String city;
	private String name;
	private String province;
	private String avatar_large;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
}
